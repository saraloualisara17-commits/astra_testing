package com.wash.laundry_app.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommandeStatusRequest {

    private CommandeStatus status;
    private String commentaire;
    private BigDecimal montantCollecte;
    private String notesPaiement;

    public CommandeStatus getStatus() { return status; }
    public void setStatus(CommandeStatus status) { this.status = status; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public BigDecimal getMontantCollecte() { return montantCollecte; }
    public void setMontantCollecte(BigDecimal montantCollecte) { this.montantCollecte = montantCollecte; }
    public String getNotesPaiement() { return notesPaiement; }
    public void setNotesPaiement(String notesPaiement) { this.notesPaiement = notesPaiement; }
}
