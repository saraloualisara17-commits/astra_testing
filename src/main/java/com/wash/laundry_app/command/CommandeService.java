package com.wash.laundry_app.command;

import com.wash.laundry_app.command.attempts.CancelOrderRequest;
import com.wash.laundry_app.command.attempts.OrderAttemptDto;
import com.wash.laundry_app.command.attempts.OrderAttemptRequest;
import com.wash.laundry_app.command.attempts.OrderAttemptService;
import com.wash.laundry_app.command.services.*;
import com.wash.laundry_app.users.employe.CommandDetails;
import com.wash.laundry_app.users.lvreur.LivreurDashboardStatsDTO;
import com.wash.laundry_app.users.admin.AdminOrdersResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CommandeService {

    private final CommandeCreationService creationService;
    private final CommandeWorkflowService workflowService;
    private final CommandePaymentService paymentService;
    private final CommandeQueryService queryService;
    private final CommandeImageService imageService;
    private final OrderAttemptService attemptService;

    public CommandeDTO createCommande(CreateCommandeRequest request) {
        return creationService.createCommande(request);
    }

    public CommandDetails getCommandeById(Long id) {
        return queryService.getCommandeById(id);
    }

    public CommandeDTO getCommandeDtoById(Long id) {
        return queryService.getCommandeDtoById(id);
    }

    public List<CommandeDTO> getAllCommandes() {
        return queryService.getAllCommandes();
    }

    public List<CommandeDTO> getCommandesByLivreur(Long livreurId) {
        return queryService.getCommandesByLivreur(livreurId);
    }

    public List<CommandeDTO> getCommandesByStatus(CommandeStatus status) {
        return queryService.getCommandesByStatus(status);
    }

    public long getCountByStatus(CommandeStatus status) {
        return queryService.getCountByStatus(status);
    }

    public CommandeDTO updateStatus(Long id, UpdateCommandeStatusRequest request) {
        return workflowService.updateStatus(id, request);
    }

    public CommandeDTO updateCommande(Long id, UpdateCommandeRequest request) {
        return workflowService.updateCommande(id, request);
    }

    public void deleteCommande(Long id) {
        workflowService.deleteCommande(id);
    }

    public CommandeDTO annulerCommande(Long id) {
        return workflowService.annulerCommande(id);
    }

    public CommandeDTO returnToWorkplace(Long id) {
        return workflowService.returnToWorkplace(id);
    }

    public CommandeDTO updateItemsByPickupDriver(Long id, List<CreateCommandeRequest.TapisItem> items) {
        return workflowService.updateItemsByPickupDriver(id, items);
    }

    public List<CommandeDTO> getCancelledOrdersForPickupDriver() {
        return queryService.getCancelledOrdersForPickupDriver();
    }

    public long getReadyForDeliveryCountForDeliveryDriver() {
        return queryService.getReadyForDeliveryCountForDeliveryDriver();
    }

    public Commande getById(Long id) {
        return queryService.getById(id);
    }

    public CommandeDTO recordPayment(Long id, RecordPaymentRequest request) {
        return paymentService.recordPayment(id, request);
    }

    public PaiementDTO addPayment(Long id, BigDecimal amount, String note, ModePaiement modePaiement) {
        return paymentService.addPayment(id, amount, note, modePaiement);
    }

    public List<PaiementDTO> getPayments(Long id) {
        return paymentService.getPayments(id);
    }

    public List<HistoriqueStatutDTO> getHistory(Long id) {
        return queryService.getHistory(id);
    }

    public void addOrderImages(Long id, List<String> imageUrls, String photoType) {
        imageService.addOrderImages(id, imageUrls, photoType);
    }

    public List<CommandeDTO> getPendingPickupOrdersForPickupDriver() {
        return queryService.getPendingPickupOrdersForPickupDriver();
    }

    public List<CommandeDTO> getReadyForDeliveryByDeliveryDriver() {
        return queryService.getReadyForDeliveryByDeliveryDriver();
    }

    public List<CommandeDTO> getPastDeliveriesForDriver() {
        return queryService.getPastDeliveriesForDriver();
    }

    public LivreurDashboardStatsDTO getLivreurDashboardStats() {
        return queryService.getLivreurDashboardStats();
    }

    public List<PaymentTypeDTO> getPaymentTypes() {
        return queryService.getPaymentTypes();
    }

    public AdminOrdersResponseDTO getFilteredCommands(
            String status, String mode, Boolean paidDebts, Boolean activeOnly, Boolean selfSubmitted,
            LocalDate dateDebut, LocalDate dateFin,
            String search, Long livreurId, int page, int size, String sortDirection) {
        return queryService.getFilteredCommands(status, mode, paidDebts, activeOnly, selfSubmitted,
                dateDebut, dateFin, search, livreurId, page, size, sortDirection);
    }

    public OrderAttemptDto recordFailedAttempt(Long id, OrderAttemptRequest request) {
        return attemptService.recordFailedAttempt(id, request);
    }

    public CommandeDTO cancelOrderWithReason(Long id, CancelOrderRequest request) {
        return attemptService.cancelOrderWithReason(id, request);
    }

    public List<OrderAttemptDto> getAttemptsForOrder(Long id) {
        return attemptService.getAttemptsForOrder(id);
    }
}
