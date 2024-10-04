package tn.esprit.PFE.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table()
@NoArgsConstructor
@AllArgsConstructor
public class Produits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    @Column(length = 1500)
    private String description;
    private float prix;
    @Column(length = 1500)
    private String lien;
    private Long id_user;
    private String image_vitrine;
    private int etat; // 0: demande, 1: refusée, 2: acceptée, 3: archivée
    private boolean active;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp DateCreation;
    @UpdateTimestamp
    private Timestamp DateModification;
    @Transient
    private boolean favoris;

    private Long originalProduitId;
    @Column(name = "is_new_version")
    private boolean isNewVersion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "produits", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProduitsAssets> produitAssets = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Categories> categories;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Plateforme> plateformes;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Technologies> technologies;


    public void addProduitsAssets(ProduitsAssets asset) {
        produitAssets.add(asset);
        asset.setProduits(this);
    }

    public void removeProduitsAssets(ProduitsAssets asset) {
        produitAssets.remove(asset);
        asset.setProduits(null);
    }

}
