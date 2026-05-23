package com.wash.laundry_app.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommandeRequest {

    @NotNull(message = "L'ID du client est obligatoire")
    private Long clientId;

    @NotEmpty(message = "Au moins un tapis est requis")
    @Valid
    private List<TapisItem> tapis;

    private String mode;
    private Long pickupDriverId;
    private Long deliveryDriverId;
    private String paymentMethod;
    private String notes;
    private String deliveryType;
    private String deliveryAddress;
    private BigDecimal deliveryLatitude;
    private BigDecimal deliveryLongitude;
    private BigDecimal montantPaye;
    private List<String> imageUrls;

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public List<TapisItem> getTapis() { return tapis; }
    public void setTapis(List<TapisItem> tapis) { this.tapis = tapis; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public Long getPickupDriverId() { return pickupDriverId; }
    public void setPickupDriverId(Long pickupDriverId) { this.pickupDriverId = pickupDriverId; }
    public Long getDeliveryDriverId() { return deliveryDriverId; }
    public void setDeliveryDriverId(Long deliveryDriverId) { this.deliveryDriverId = deliveryDriverId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getDeliveryType() { return deliveryType; }
    public void setDeliveryType(String deliveryType) { this.deliveryType = deliveryType; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public BigDecimal getDeliveryLatitude() { return deliveryLatitude; }
    public void setDeliveryLatitude(BigDecimal deliveryLatitude) { this.deliveryLatitude = deliveryLatitude; }
    public BigDecimal getDeliveryLongitude() { return deliveryLongitude; }
    public void setDeliveryLongitude(BigDecimal deliveryLongitude) { this.deliveryLongitude = deliveryLongitude; }
    public BigDecimal getMontantPaye() { return montantPaye; }
    public void setMontantPaye(BigDecimal montantPaye) { this.montantPaye = montantPaye; }
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TapisItem {

        private Long productId;

        private String nom;
        private String description;
        private BigDecimal prixUnitaire;

        @NotNull(message = "La quantité est obligatoire")
        private Integer quantite;

        private List<String> imageUrls;
        private Integer mainImageIndex;
        private Long carpetTypeId;

        private BigDecimal largeur;
        private BigDecimal hauteur;
        private BigDecimal longueur;
        private BigDecimal poids;
        private BigDecimal prixCalcule;
        private BigDecimal prixFinal;
        private BigDecimal manualPrice;
        private ModeTarification modeTarification;

        private String tagNumero;
        private String notes;
        private String couleur;
        private BigDecimal remiseMontant;
        private String remiseRaison;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public BigDecimal getPrixUnitaire() { return prixUnitaire; }
        public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
        public Integer getQuantite() { return quantite; }
        public void setQuantite(Integer quantite) { this.quantite = quantite; }
        public List<String> getImageUrls() { return imageUrls; }
        public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
        public Integer getMainImageIndex() { return mainImageIndex; }
        public void setMainImageIndex(Integer mainImageIndex) { this.mainImageIndex = mainImageIndex; }
        public Long getCarpetTypeId() { return carpetTypeId; }
        public void setCarpetTypeId(Long carpetTypeId) { this.carpetTypeId = carpetTypeId; }
        public BigDecimal getLargeur() { return largeur; }
        public void setLargeur(BigDecimal largeur) { this.largeur = largeur; }
        public BigDecimal getHauteur() { return hauteur; }
        public void setHauteur(BigDecimal hauteur) { this.hauteur = hauteur; }
        public BigDecimal getLongueur() { return longueur; }
        public void setLongueur(BigDecimal longueur) { this.longueur = longueur; }
        public BigDecimal getPoids() { return poids; }
        public void setPoids(BigDecimal poids) { this.poids = poids; }
        public BigDecimal getPrixCalcule() { return prixCalcule; }
        public void setPrixCalcule(BigDecimal prixCalcule) { this.prixCalcule = prixCalcule; }
        public BigDecimal getPrixFinal() { return prixFinal; }
        public void setPrixFinal(BigDecimal prixFinal) { this.prixFinal = prixFinal; }
        public BigDecimal getManualPrice() { return manualPrice; }
        public void setManualPrice(BigDecimal manualPrice) { this.manualPrice = manualPrice; }
        public ModeTarification getModeTarification() { return modeTarification; }
        public void setModeTarification(ModeTarification modeTarification) { this.modeTarification = modeTarification; }
        public String getTagNumero() { return tagNumero; }
        public void setTagNumero(String tagNumero) { this.tagNumero = tagNumero; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public String getCouleur() { return couleur; }
        public void setCouleur(String couleur) { this.couleur = couleur; }
        public BigDecimal getRemiseMontant() { return remiseMontant; }
        public void setRemiseMontant(BigDecimal remiseMontant) { this.remiseMontant = remiseMontant; }
        public String getRemiseRaison() { return remiseRaison; }
        public void setRemiseRaison(String remiseRaison) { this.remiseRaison = remiseRaison; }
    }
}
