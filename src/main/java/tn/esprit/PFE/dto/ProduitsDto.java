package tn.esprit.PFE.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tn.esprit.PFE.entities.*;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProduitsDto {
    private Long id;
    private String nom;
    private String description;
    private float prix;
    private String lien;
    private Long id_user;
    private String image_vitrine;
    private String plateforme;
    private String categorie;
    private List<Technologies> technologies;
    private int etat;
    private boolean active;
    private Timestamp DateCreation;
    private Timestamp DateModification;

    public ProduitsDto(Produits produits) {
        this.id = produits.getId();
        this.nom = produits.getNom();
        this.description = produits.getDescription();
        this.prix = produits.getPrix();
        this.lien=produits.getLien();
        this.id_user = produits.getUser().getId();
        this.image_vitrine = produits.getImage_vitrine();
        this.etat = produits.getEtat();
        this.active = produits.isActive();
        this.DateCreation = produits.getDateCreation();
        this.DateModification = produits.getDateModification();
        this.categorie= produits.getCategories().stream()
                .findFirst()
                .map(Categories::getNom)
                .orElse(null);
        this.plateforme=produits.getPlateformes().stream()
                .findFirst()
                .map(Plateforme::getNom)
                .orElse(null);
        this.technologies=produits.getTechnologies();
    }
}
