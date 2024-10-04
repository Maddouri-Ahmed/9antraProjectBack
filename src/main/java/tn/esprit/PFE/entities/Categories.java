package tn.esprit.PFE.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table()
@NoArgsConstructor
@AllArgsConstructor
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image_url;
    private String nom;
    @Column(length = 500)
    private String description;
    private boolean active;
        private boolean meilleur;
    @Transient
    private int produitCount;
    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
    private Set<Produits> produits = new HashSet<>();

}
