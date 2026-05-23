package com.wash.laundry_app.statistiques;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsDTO {
    private Long totalCommandesToday;
    private BigDecimal revenuesToday;
    private Long totalClients;
    private Long commandesEnAttente;
    private Long commandesValidees;
    private Long commandesEnTraitement;
    private Long commandesPretes;
    private Long commandesLivrees;
    private Long commandesPayees;
    private Long totalCommandes;
    private BigDecimal totalRevenue;
    private BigDecimal totalRevenues;
    private Map<String, Long> commandesByStatus;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Map<String, Object> unpaid;
    private Long recuesCount;
    private Long recuesItems;
    private BigDecimal recuesM2;
    private BigDecimal recuesTotal;
    private Long livreesCount;
    private Long livreesItems;
    private BigDecimal livreesM2;
    private BigDecimal livreesTotal;
    private BigDecimal livreesPaid;
    private BigDecimal livreesUnpaid;
}
