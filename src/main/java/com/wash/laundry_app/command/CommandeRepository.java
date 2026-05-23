package com.wash.laundry_app.command;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    boolean existsByClientId(Long clientId);

    Optional<Commande> findByNumeroCommande(String numeroCommande);

    boolean existsByNumeroCommande(String numeroCommande);

    @Query("SELECT c FROM Commande c LEFT JOIN FETCH c.client cl LEFT JOIN FETCH cl.phones WHERE c.id = :id")
    Optional<Commande> findWithClientDetailsById(@Param("id") Long id);

    List<Commande> findByLivreurId(Long livreurId);

    List<Commande> findByLivreurIdAndStatus(Long livreurId, CommandeStatus status);

    List<Commande> findByLivreurIdAndStatusIn(Long livreurId, List<CommandeStatus> statuses);

    long countByLivreurIdAndStatus(Long livreurId, CommandeStatus status);

    List<Commande> findByDeliveryDriverIdAndStatus(Long deliveryDriverId, CommandeStatus status);

    long countByDeliveryDriverIdAndStatus(Long deliveryDriverId, CommandeStatus status);

    List<Commande> findByClientId(Long clientId);

    List<Commande> findByStatus(CommandeStatus status);

    List<Commande> findByStatusIn(List<CommandeStatus> statuses);

    @Query("SELECT c FROM Commande c WHERE c.status = :status AND c.deliveryDriver.id = :driverId")
    List<Commande> findReadyForDeliveryDueForDriver(@Param("driverId") Long driverId);

    List<Commande> findByDateCreationBetween(LocalDateTime start, LocalDateTime end);

    long countByStatus(CommandeStatus status);

    @Query("SELECT COUNT(c) FROM Commande c WHERE DATE(c.dateCreation) = CURRENT_DATE")
    long countTodayCommandes();

    @Query("SELECT SUM(c.montantTotal) FROM Commande c WHERE c.status = 'DELIVERED' AND DATE(c.dateLivraison) = CURRENT_DATE")
    Double getTodayRevenue();

    @Query("SELECT COUNT(c) FROM Commande c WHERE c.status = :status AND DATE(c.dateCreation) = CURRENT_DATE")
    long countTodayCommandesByStatus(@Param("status") CommandeStatus status);

    @Query("SELECT COUNT(c) FROM Commande c WHERE DATE(c.dateCreation) = :date")
    long countCommandesByDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(c.montantTotal) FROM Commande c WHERE c.status = 'DELIVERED' AND DATE(c.dateLivraison) = :date")
    Double getRevenueByDate(@Param("date") LocalDate date);

    List<Commande> findByLivreurIdAndDateCreationBetween(Long livreurId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT c FROM Commande c WHERE " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:mode IS NULL OR c.mode = :mode) AND " +
           "(:activeOnly IS NULL OR :activeOnly = false OR c.status NOT IN ('DELIVERED', 'CANCELLED')) AND " +
           "(:paidDebts IS NULL OR :paidDebts = false OR (c.montantTotal > c.montantPaye AND c.status != 'CANCELLED')) AND " +
           "(:selfSubmitted IS NULL OR :selfSubmitted = false) AND " +
           "(:since IS NULL OR c.dateCreation >= :since) AND " +
           "(:dateDebut IS NULL OR c.dateCreation >= :dateDebut) AND " +
           "(:dateFin IS NULL OR c.dateCreation <= :dateFin) AND " +
           "(:livreurId IS NULL OR c.livreur.id = :livreurId OR c.pickupDriver.id = :livreurId OR c.deliveryDriver.id = :livreurId) AND " +
           "(:search IS NULL OR LOWER(c.numeroCommande) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.client.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Commande> findFiltered(
            @Param("status") CommandeStatus status,
            @Param("mode") ModeCommande mode,
            @Param("activeOnly") Boolean activeOnly,
            @Param("paidDebts") Boolean paidDebts,
            @Param("selfSubmitted") Boolean selfSubmitted,
            @Param("since") LocalDateTime since,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("search") String search,
            @Param("livreurId") Long livreurId,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(c.montantTotal), 0) FROM Commande c WHERE " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:mode IS NULL OR c.mode = :mode) AND " +
           "(:activeOnly IS NULL OR :activeOnly = false OR c.status NOT IN ('DELIVERED', 'CANCELLED')) AND " +
           "(:paidDebts IS NULL OR :paidDebts = false OR (c.montantTotal > c.montantPaye AND c.status != 'CANCELLED')) AND " +
           "(:selfSubmitted IS NULL OR :selfSubmitted = false) AND " +
           "(:since IS NULL OR c.dateCreation >= :since) AND " +
           "(:dateDebut IS NULL OR c.dateCreation >= :dateDebut) AND " +
           "(:dateFin IS NULL OR c.dateCreation <= :dateFin) AND " +
           "(:livreurId IS NULL OR c.livreur.id = :livreurId OR c.pickupDriver.id = :livreurId OR c.deliveryDriver.id = :livreurId) AND " +
           "(:search IS NULL OR LOWER(c.numeroCommande) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.client.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    BigDecimal findFilteredTotalValue(
            @Param("status") CommandeStatus status,
            @Param("mode") ModeCommande mode,
            @Param("activeOnly") Boolean activeOnly,
            @Param("paidDebts") Boolean paidDebts,
            @Param("selfSubmitted") Boolean selfSubmitted,
            @Param("since") LocalDateTime since,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("search") String search,
            @Param("livreurId") Long livreurId
    );

    @Query("SELECT COALESCE(SUM(c.montantTotal - COALESCE(c.montantPaye, 0)), 0) FROM Commande c WHERE " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:mode IS NULL OR c.mode = :mode) AND " +
           "(:activeOnly IS NULL OR :activeOnly = false OR c.status NOT IN ('DELIVERED', 'CANCELLED')) AND " +
           "(:paidDebts IS NULL OR :paidDebts = false OR (c.montantTotal > c.montantPaye AND c.status != 'CANCELLED')) AND " +
           "(:selfSubmitted IS NULL OR :selfSubmitted = false) AND " +
           "(:since IS NULL OR c.dateCreation >= :since) AND " +
           "(:dateDebut IS NULL OR c.dateCreation >= :dateDebut) AND " +
           "(:dateFin IS NULL OR c.dateCreation <= :dateFin) AND " +
           "(:livreurId IS NULL OR c.livreur.id = :livreurId OR c.pickupDriver.id = :livreurId OR c.deliveryDriver.id = :livreurId) AND " +
           "(:search IS NULL OR LOWER(c.numeroCommande) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.client.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    BigDecimal findFilteredTotalUnpaid(
            @Param("status") CommandeStatus status,
            @Param("mode") ModeCommande mode,
            @Param("activeOnly") Boolean activeOnly,
            @Param("paidDebts") Boolean paidDebts,
            @Param("selfSubmitted") Boolean selfSubmitted,
            @Param("since") LocalDateTime since,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("search") String search,
            @Param("livreurId") Long livreurId
    );

    @Query("SELECT COALESCE(COUNT(ct), 0) FROM Commande c JOIN c.commandeTapis ct WHERE " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:mode IS NULL OR c.mode = :mode) AND " +
           "(:activeOnly IS NULL OR :activeOnly = false OR c.status NOT IN ('DELIVERED', 'CANCELLED')) AND " +
           "(:paidDebts IS NULL OR :paidDebts = false OR (c.montantTotal > c.montantPaye AND c.status != 'CANCELLED')) AND " +
           "(:selfSubmitted IS NULL OR :selfSubmitted = false) AND " +
           "(:since IS NULL OR c.dateCreation >= :since) AND " +
           "(:dateDebut IS NULL OR c.dateCreation >= :dateDebut) AND " +
           "(:dateFin IS NULL OR c.dateCreation <= :dateFin) AND " +
           "(:livreurId IS NULL OR c.livreur.id = :livreurId OR c.pickupDriver.id = :livreurId OR c.deliveryDriver.id = :livreurId) AND " +
           "(:search IS NULL OR LOWER(c.numeroCommande) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.client.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Long findFilteredTotalVolume(
            @Param("status") CommandeStatus status,
            @Param("mode") ModeCommande mode,
            @Param("activeOnly") Boolean activeOnly,
            @Param("paidDebts") Boolean paidDebts,
            @Param("selfSubmitted") Boolean selfSubmitted,
            @Param("since") LocalDateTime since,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("search") String search,
            @Param("livreurId") Long livreurId
    );

    @Query("SELECT c.client.id as clientId, COUNT(c) as orderCount, MAX(c.dateCreation) as lastOrderDate FROM Commande c WHERE c.client.id IN :clientIds GROUP BY c.client.id")
    List<Object[]> findOrderStatsByClientIds(@Param("clientIds") List<Long> clientIds);

    @Query("SELECT c FROM Commande c LEFT JOIN FETCH c.client cl LEFT JOIN FETCH cl.phones LEFT JOIN FETCH c.commandeTapis WHERE c.id = :id")
    Optional<Commande> findForReceiptById(@Param("id") Long id);

    List<Commande> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(ct) FROM Commande c JOIN c.commandeTapis ct WHERE DATE(c.dateCreation) = :date")
    long countItemsByDate(@Param("date") LocalDate date);

    @Query("SELECT c FROM Commande c WHERE c.montantTotal > COALESCE(c.montantPaye, 0) AND c.status != 'CANCELLED'")
    List<Commande> findAllWithUnpaidBalance();

    @Query("SELECT COUNT(DISTINCT c.client.id) FROM Commande c WHERE c.montantTotal > COALESCE(c.montantPaye, 0) AND c.status != 'CANCELLED'")
    long countClientsWithDebt();

    @Query("SELECT c.client.id, c.client.name, SUM(c.montantTotal - COALESCE(c.montantPaye, 0)) FROM Commande c WHERE c.montantTotal > COALESCE(c.montantPaye, 0) AND c.status != 'CANCELLED' GROUP BY c.client.id, c.client.name")
    List<Object[]> findClientDebtSummary();

    @Query("SELECT c FROM Commande c WHERE c.client.id = :clientId AND c.montantTotal > COALESCE(c.montantPaye, 0) AND c.status != 'CANCELLED'")
    List<Commande> findUnpaidByClient(@Param("clientId") Long clientId);

    @Query("SELECT c FROM Commande c WHERE c.deliveryLatitude IS NOT NULL AND c.deliveryLongitude IS NOT NULL")
    List<Commande> findAllWithGpsCoordinates();
}
