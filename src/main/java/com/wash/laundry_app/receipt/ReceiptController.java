package com.wash.laundry_app.receipt;

import com.wash.laundry_app.command.Commande;
import com.wash.laundry_app.command.CommandeNotFoundException;
import com.wash.laundry_app.command.CommandeRepository;
import com.wash.laundry_app.command.CommandeStatus;
import com.wash.laundry_app.command.Paiement;
import com.wash.laundry_app.command.PaiementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/commandes/{id}/receipt")
@RequiredArgsConstructor
public class ReceiptController {

    private final CommandeRepository commandeRepository;
    private final PdfService pdfService;
    private final PaiementRepository paiementRepository;

    private String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "anonymous";
    }

    // Each bag (@OneToMany List) must be fetched in its own query.
    // Hibernate throws "cannot simultaneously fetch multiple bags" if two or more
    // List collections are join-fetched in the same JPQL query.
    private Commande loadForReceipt(Long id) {
        Commande commande = commandeRepository.findForReceiptById(id)
                .orElseThrow(CommandeNotFoundException::new);
        commandeRepository.findWithItemsById(id)
                .ifPresent(c -> commande.setCommandeTapis(c.getCommandeTapis()));
        commandeRepository.findWithPaiementsById(id)
                .ifPresent(c -> commande.setPaiements(c.getPaiements()));
        commandeRepository.findWithAttemptsById(id)
                .ifPresent(c -> commande.setAttempts(c.getAttempts()));
        return commande;
    }

    @Transactional(readOnly = true)
    @GetMapping("/order/pdf")
    public ResponseEntity<byte[]> getOrderReceiptPdf(@PathVariable Long id) {
        log.info("[receipt] ORDER PDF requested — orderId={} user={}", id, currentUser());
        long t0 = System.currentTimeMillis();
        Commande commande = loadForReceipt(id);
        log.info("[receipt] ORDER PDF load complete — orderId={} status={} items={} payments={}",
                id, commande.getStatus(),
                commande.getCommandeTapis() != null ? commande.getCommandeTapis().size() : 0,
                commande.getPaiements() != null ? commande.getPaiements().size() : 0);
        byte[] pdf = pdfService.generatePdf(commande, "ORDER");
        log.info("[receipt] ORDER PDF generated — orderId={} bytes={} durationMs={}",
                id, pdf.length, System.currentTimeMillis() - t0);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=commande-" + commande.getNumeroCommande() + ".pdf")
                .body(pdf);
    }

    @Transactional(readOnly = true)
    @GetMapping("/delivery/pdf")
    public ResponseEntity<byte[]> getDeliveryNotePdf(@PathVariable Long id) {
        log.info("[receipt] DELIVERY PDF requested — orderId={} user={}", id, currentUser());
        long t0 = System.currentTimeMillis();
        Commande commande = loadForReceipt(id);
        if (commande.getStatus() != CommandeStatus.DELIVERED) {
            log.warn("[receipt] DELIVERY PDF rejected — orderId={} currentStatus={} (must be DELIVERED)",
                    id, commande.getStatus());
            return ResponseEntity.badRequest().build();
        }
        byte[] pdf = pdfService.generatePdf(commande, "DELIVERY");
        log.info("[receipt] DELIVERY PDF generated — orderId={} bytes={} durationMs={}",
                id, pdf.length, System.currentTimeMillis() - t0);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=livraison-" + commande.getNumeroCommande() + ".pdf")
                .body(pdf);
    }

    @Transactional(readOnly = true)
    @GetMapping("/order/whatsapp")
    public ResponseEntity<?> getOrderWhatsAppMeta(@PathVariable Long id) {
        Commande commande = loadForReceipt(id);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", Map.of(
                "phone", pdfService.getClientWhatsAppPhone(commande) != null ? pdfService.getClientWhatsAppPhone(commande) : "",
                "message", pdfService.generateWhatsAppMessage(commande, "ORDER"),
                "pdfUrl", "/api/commandes/" + id + "/receipt/order/pdf"
            )
        ));
    }

    @Transactional(readOnly = true)
    @GetMapping("/delivery/whatsapp")
    public ResponseEntity<?> getDeliveryWhatsAppMeta(@PathVariable Long id) {
        Commande commande = loadForReceipt(id);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", Map.of(
                "phone", pdfService.getClientWhatsAppPhone(commande) != null ? pdfService.getClientWhatsAppPhone(commande) : "",
                "message", pdfService.generateWhatsAppMessage(commande, "DELIVERY"),
                "pdfUrl", "/api/commandes/" + id + "/receipt/delivery/pdf"
            )
        ));
    }

    @Transactional(readOnly = true)
    @GetMapping("/order/thermal")
    public ResponseEntity<String> getOrderReceiptThermal(@PathVariable Long id) {
        Commande commande = loadForReceipt(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                .body(pdfService.buildThermalReceiptText(commande, "ORDER"));
    }

    @Transactional(readOnly = true)
    @GetMapping("/delivery/thermal")
    public ResponseEntity<String> getDeliveryNoteThermal(@PathVariable Long id) {
        Commande commande = loadForReceipt(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                .body(pdfService.buildThermalReceiptText(commande, "DELIVERY"));
    }

    @Transactional(readOnly = true)
    @GetMapping("/payments/{paymentId}/pdf")
    public ResponseEntity<byte[]> getPaymentReceiptPdf(@PathVariable Long id, @PathVariable Long paymentId) {
        Commande commande = loadForReceipt(id);
        Paiement paiement = commande.getPaiements().stream()
                .filter(p -> p.getId().equals(paymentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));
        byte[] pdf = pdfService.generatePaymentReceiptPdf(paiement);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=paiement-" + paymentId + ".pdf")
                .body(pdf);
    }

}
