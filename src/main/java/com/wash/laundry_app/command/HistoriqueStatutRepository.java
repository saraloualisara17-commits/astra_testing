package com.wash.laundry_app.command;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoriqueStatutRepository extends JpaRepository<HistoriqueStatut, Long> {

    List<HistoriqueStatut> findByCommandeIdOrderByCreatedAtDesc(Long commandeId);

    List<HistoriqueStatut> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT h FROM HistoriqueStatut h WHERE h.nouveauStatut = 'READY_FOR_DELIVERY' AND h.commande.id IN :commandeIds")
    List<HistoriqueStatut> findPreteHistoryForCommandes(@Param("commandeIds") List<Long> commandeIds);

    @Query("SELECT DISTINCT h.commande.id FROM HistoriqueStatut h WHERE h.nouveauStatut = :status AND h.createdAt BETWEEN :start AND :end")
    List<Long> findDistinctCommandeIdsByStatusBetween(@Param("status") String status, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(DISTINCT h.commande.id) FROM HistoriqueStatut h WHERE h.nouveauStatut = :status AND h.createdAt BETWEEN :start AND :end")
    long countDistinctCommandesByStatusBetween(@Param("status") String status, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
