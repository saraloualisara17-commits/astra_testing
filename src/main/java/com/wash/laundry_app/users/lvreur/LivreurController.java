package com.wash.laundry_app.users.lvreur;

import com.wash.laundry_app.auth.AuthService;
import com.wash.laundry_app.command.*;
import com.wash.laundry_app.config.FileStorageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/livreur")
@AllArgsConstructor
public class LivreurController {

    private final CommandeService commandeService;
    private final AuthService authService;
    private final FileStorageService fileStorageService;

    @GetMapping("/commandes/ready-for-delivery")
    public ResponseEntity<List<CommandeDTO>> getReadyForDelivery() {
        return ResponseEntity.ok(commandeService.getReadyForDeliveryByDeliveryDriver());
    }

    @PostMapping("/commandes/{id}/payment")
    public ResponseEntity<CommandeDTO> recordPayment(
            @PathVariable Long id,
            @Valid @RequestBody RecordPaymentRequest request) {
        return ResponseEntity.ok(commandeService.recordPayment(id, request));
    }

    @PutMapping("/commandes/{id}/annuler")
    public ResponseEntity<CommandeDTO> annulerCommande(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.annulerCommande(id));
    }

    @GetMapping("/commandes/prete-count")
    public ResponseEntity<Map<String, Long>> getPreteCount() {
        long count = commandeService.getLivreurDashboardStats().getReadyOrdersCount();
        return ResponseEntity.ok(Map.of("readyOrdersCount", count));
    }

    @GetMapping("/commandes/prete")
    public ResponseEntity<List<CommandeDTO>> getReadyOrders() {
        return ResponseEntity.ok(commandeService.getReadyForDeliveryByDeliveryDriver());
    }

    @GetMapping("/commandes/canceled-deliveries")
    public ResponseEntity<List<CommandeDTO>> getCanceledDeliveries() {
        return ResponseEntity.ok(commandeService.getPastDeliveriesForDriver());
    }

    @GetMapping("/commandes/pending-pickup")
    public ResponseEntity<List<CommandeDTO>> getPendingPickup() {
        return ResponseEntity.ok(commandeService.getPendingPickupOrdersForPickupDriver());
    }

    @GetMapping("/commandes/past-deliveries")
    public ResponseEntity<List<CommandeDTO>> getPastDeliveries() {
        return ResponseEntity.ok(commandeService.getPastDeliveriesForDriver());
    }

    @PatchMapping("/commandes/{id}/return")
    public ResponseEntity<CommandeDTO> returnToWorkplace(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.returnToWorkplace(id));
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<LivreurDashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(commandeService.getLivreurDashboardStats());
    }

    @GetMapping("/payment-types")
    public ResponseEntity<List<PaymentTypeDTO>> getPaymentTypes() {
        return ResponseEntity.ok(commandeService.getPaymentTypes());
    }

    @PatchMapping("/commandes/{id}/items")
    public ResponseEntity<CommandeDTO> updateOrderItems(
            @PathVariable Long id,
            @RequestBody List<CreateCommandeRequest.TapisItem> items) {
        return ResponseEntity.ok(commandeService.updateItemsByPickupDriver(id, items));
    }

    @PostMapping("/tapis/upload")
    public ResponseEntity<List<Map<String, String>>> uploadTapisImages(@RequestParam("files") MultipartFile[] files) {
        List<Map<String, String>> result = Arrays.stream(files).map(file -> {
            String fileName = fileStorageService.storeFile(file);
            return Map.of("imageUrl", "/uploads/" + fileName);
        }).toList();
        return ResponseEntity.ok(result);
    }
}
