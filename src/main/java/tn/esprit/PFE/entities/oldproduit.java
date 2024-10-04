package tn.esprit.PFE.entities;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@ToString
@Table(name="produit")
@NoArgsConstructor
@AllArgsConstructor
public class oldproduit {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)


    private Long id ;
    private String name;
    private String description;
    private String typev;
    private float prix;
    private long iduser;
    private boolean pending;
    @Enumerated(EnumType.STRING)
    private Outil outil;
    @Enumerated(EnumType.STRING)
    private Outil outil1;
    @Enumerated(EnumType.STRING)
    private Outil outil2;
    @Enumerated(EnumType.STRING)
    private Outil outil3;
    @Enumerated(EnumType.STRING)
    private Outil outil4;
    @Enumerated(EnumType.STRING)
    private Categore categore;
    @Enumerated(EnumType.STRING)
    private catégories catégories;
    @Enumerated(EnumType.STRING)
    private catégoriesDG catégoriesDG;
    @Enumerated(EnumType.STRING)
    private Mobileapps Mobileapps;
    private LocalDate date;
    private LocalTime time;
    private String fileName;
    private String fileName1;
    private String fileName2;
    private String fileName3;
    private String fileVideo;
    private String code;




    // currentDate = new Date();
   // {{currentDate | date:'yyyy-MM-dd'}}

}
