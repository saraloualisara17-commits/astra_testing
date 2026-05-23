package com.wash.laundry_app.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeTapisDTO {

    private Long id;
    private Long productId;
    private String productNom;
    private String productPricingMethod;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;
    private BigDecimal largeur;
    private BigDecimal hauteur;
    private BigDecimal longueur;
    private BigDecimal poids;
    private BigDecimal remiseMontant;
    private String remiseRaison;
    private String tagNumero;
    private String notes;
    private String couleur;
    private List<CommandeImageDTO> images;
    private BigDecimal prixCalcule;
    private BigDecimal prixFinal;
    private ModeTarification modeTarification;
    private BigDecimal surface;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
