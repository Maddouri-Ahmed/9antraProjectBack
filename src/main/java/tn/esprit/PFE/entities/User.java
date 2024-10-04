package tn.esprit.PFE.entities;


import lombok.*;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.enabled;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
        private String username;
    private String fullname;
    private String password;
    private String email;
    private String adressUser;
    private String city;
    private String facebook;
    private String instagram;
    private String linkedin;
    private String github;
    private String portfolio;
    private String about;
    private double phoneNumberUser;
    private boolean isEmailValidated = false;


    private Random code;
    private boolean contri;
    Long idAdministrateur;
    private String fileName;
    @Enumerated(EnumType.STRING)
    private StatusUser statusUser = StatusUser.ACTIVE;
    private String competence;
    private Boolean archiver;
    @Enumerated(EnumType.STRING)
    private SexeType sexeUser;
    @OneToOne(  fetch = FetchType.EAGER, cascade = CascadeType.ALL  )
    private Role roles;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Boit_tchat> boit_tchat ;
    private String confirmationCode;

    private String bibliographie;

    private boolean emailVerified = false;
    private Date verificationTokenExpiry;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    @Column(name = "is_enable")
    private Boolean isEnable = false;




}
