package tn.esprit.PFE.entities;

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
public class Plateforme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image_url;
    private String nom;
    private boolean active;

    @Column(name = "type", columnDefinition = "VARCHAR(50)")
    @Enumerated(EnumType.STRING)
    private PlateformeType type;

//    @ManyToMany(mappedBy = "plateformes")
//    private Set<Produits> produits = new HashSet<>();

}
