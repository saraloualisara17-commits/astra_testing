package com.wash.laundry_app.command;

import com.wash.laundry_app.clients.Client;
import com.wash.laundry_app.command.attempts.OrderAttempt;
import com.wash.laundry_app.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "numero_commande", unique = true, nullable = false, length = 50)
    private String numeroCommande;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommandeStatus status = CommandeStatus.PENDING_PICKUP;

    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @Column(name = "date_livraison")
    private LocalDateTime dateLivraison;

    @Column(name = "montant_total", precision = 10, scale = 2)
    private BigDecimal montantTotal = BigDecimal.ZERO;

    @Column(name = "montant_paye", precision = 10, scale = 2)
    private BigDecimal montantPaye = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_paiement")
    private ModePaiement modePaiement;

    @Column(name = "date_paiement")
    private LocalDateTime datePaiement;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_commande")
    private ModeCommande mode;

    @Column(name = "delivery_type")
    private String deliveryType;

    @Column(name = "scheduled_pickup_date")
    private LocalDateTime scheduledPickupDate;

    @Column(name = "scheduled_delivery_date")
    private LocalDateTime scheduledDeliveryDate;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "delivery_address", length = 500)
    private String deliveryAddress;

    @Column(name = "delivery_latitude", precision = 10, scale = 7)
    private BigDecimal deliveryLatitude;

    @Column(name = "delivery_longitude", precision = 10, scale = 7)
    private BigDecimal deliveryLongitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    private User livreur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_driver_id")
    private User deliveryDriver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @Column(name = "self_submitted")
    private boolean selfSubmitted;

    @Version
    @Column(name = "version")
    private Long version;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandeTapis> commandeTapis = new ArrayList<>();

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandeImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paiement> paiements = new ArrayList<>();

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderAttempt> attempts = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // livreur doubles as pickupDriver
    public User getPickupDriver() { return livreur; }
    public void setPickupDriver(User pickupDriver) { this.livreur = pickupDriver; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public User getLivreur() { return livreur; }
    public void setLivreur(User livreur) { this.livreur = livreur; }
    public User getDeliveryDriver() { return deliveryDriver; }
    public void setDeliveryDriver(User deliveryDriver) { this.deliveryDriver = deliveryDriver; }
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public String getNumeroCommande() { return numeroCommande; }
    public void setNumeroCommande(String numeroCommande) { this.numeroCommande = numeroCommande; }
    public CommandeStatus getStatus() { return status; }
    public void setStatus(CommandeStatus status) { this.status = status; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public LocalDateTime getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDateTime dateValidation) { this.dateValidation = dateValidation; }
    public LocalDateTime getDateLivraison() { return dateLivraison; }
    public void setDateLivraison(LocalDateTime dateLivraison) { this.dateLivraison = dateLivraison; }
    public BigDecimal getMontantTotal() { return montantTotal; }
    public void setMontantTotal(BigDecimal montantTotal) { this.montantTotal = montantTotal; }
    public BigDecimal getMontantPaye() { return montantPaye; }
    public void setMontantPaye(BigDecimal montantPaye) { this.montantPaye = montantPaye; }
    public ModePaiement getModePaiement() { return modePaiement; }
    public void setModePaiement(ModePaiement modePaiement) { this.modePaiement = modePaiement; }
    public LocalDateTime getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDateTime datePaiement) { this.datePaiement = datePaiement; }
    public ModeCommande getMode() { return mode; }
    public void setMode(ModeCommande mode) { this.mode = mode; }
    public String getDeliveryType() { return deliveryType; }
    public void setDeliveryType(String deliveryType) { this.deliveryType = deliveryType; }
    public LocalDateTime getScheduledPickupDate() { return scheduledPickupDate; }
    public void setScheduledPickupDate(LocalDateTime scheduledPickupDate) { this.scheduledPickupDate = scheduledPickupDate; }
    public LocalDateTime getScheduledDeliveryDate() { return scheduledDeliveryDate; }
    public void setScheduledDeliveryDate(LocalDateTime scheduledDeliveryDate) { this.scheduledDeliveryDate = scheduledDeliveryDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public BigDecimal getDeliveryLatitude() { return deliveryLatitude; }
    public void setDeliveryLatitude(BigDecimal deliveryLatitude) { this.deliveryLatitude = deliveryLatitude; }
    public BigDecimal getDeliveryLongitude() { return deliveryLongitude; }
    public void setDeliveryLongitude(BigDecimal deliveryLongitude) { this.deliveryLongitude = deliveryLongitude; }
    public boolean isSelfSubmitted() { return selfSubmitted; }
    public void setSelfSubmitted(boolean selfSubmitted) { this.selfSubmitted = selfSubmitted; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public List<CommandeTapis> getCommandeTapis() { return commandeTapis; }
    public void setCommandeTapis(List<CommandeTapis> commandeTapis) { this.commandeTapis = commandeTapis; }
    public List<CommandeImage> getImages() { return images; }
    public void setImages(List<CommandeImage> images) { this.images = images; }
    public List<Paiement> getPaiements() { return paiements; }
    public void setPaiements(List<Paiement> paiements) { this.paiements = paiements; }
    public List<OrderAttempt> getAttempts() { return attempts; }
    public void setAttempts(List<OrderAttempt> attempts) { this.attempts = attempts; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        dateCreation = LocalDateTime.now();
        if (numeroCommande == null) {
            numeroCommande = generateOrderNumber();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private String generateOrderNumber() {
        return "CMD-" + LocalDateTime.now().getYear() + "-" + System.currentTimeMillis();
    }

    public void addTapis(CommandeTapis tapis) {
        commandeTapis.add(tapis);
        tapis.setCommande(this);
        recalculateTotal();
    }

    public void removeTapis(CommandeTapis tapis) {
        commandeTapis.remove(tapis);
        tapis.setCommande(null);
        recalculateTotal();
    }

    public void recalculateTotal() {
        this.montantTotal = commandeTapis.stream()
                .map(CommandeTapis::getSousTotal)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
