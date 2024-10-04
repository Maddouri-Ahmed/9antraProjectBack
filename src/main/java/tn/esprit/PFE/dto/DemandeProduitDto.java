package tn.esprit.PFE.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tn.esprit.PFE.entities.ProduitDemande;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandeProduitDto {

    private Long id;
    private String code;
    private int etat;
    private String message;
    private String adminmessage;
    private Timestamp DateCreation;
    private Timestamp DateModification;
    private Long user_id;
    private String nom_user;
    private Long id_produit;
    private String nom_produit;

    public DemandeProduitDto(ProduitDemande produitDemande) {
        this.id = produitDemande.getId();
        this.code = produitDemande.getCode();
        this.etat = produitDemande.getEtat();
        this.message = produitDemande.getMessage();
        this.adminmessage = produitDemande.getAdminmessage();
        this.DateCreation = produitDemande.getDateCreation();
        this.DateModification = produitDemande.getDateModification();
        this.user_id=produitDemande.getUser().getId();
        this.nom_user = produitDemande.getUser().getUsername();
        this.id_produit=produitDemande.getProduits().getId();
        this.nom_produit = produitDemande.getProduits().getNom();
    }
}
