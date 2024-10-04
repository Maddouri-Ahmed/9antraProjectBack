package tn.esprit.PFE.service;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.dto.ProduitsDto;
import tn.esprit.PFE.entities.Produits;

import org.springframework.data.domain.Pageable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface IProduits {
    ResponseEntity<?> AddProduit(String p, MultipartFile vitrine,List<MultipartFile> files,List<String> titles,Long iduser);

    ResponseEntity<?> AddProduitPerso(String p, Long iduser);

    ResponseEntity<?> ModifierProduit(String p, MultipartFile vitrine, List<MultipartFile> files, List<String> titles);

    void ModifyEtat(Long id, int etat);

    ResponseEntity<?> GetProduit(Long id,Long idu);

    float getNumberProducts();

    float getNumberProductsActive();

    float getNumberProductsInactive();

    float getNumberProductsDemande();

    float getNumberProductsRefuser();

    float getNumberProductsArchiver();

    float getNumberProductsAcepter();

    float getNumberProductsSupprimer();
    //ResponseEntity<?> GetAllFiltredBy(int etat1, int etat2, Pageable pageable);

    ResponseEntity<Page<ProduitsDto>> GetAllFiltredBy(
            String nom,
            Boolean active,
            Date creationStart,
            Date creationEnd,
            Date updateStart,
            Date updateEnd,
            Integer etat1,
            Integer etat2,
            Boolean isNewVersion,
            Pageable pageable
    );

    ResponseEntity<Page<ProduitsDto>> GetAllFiltredBys(
            String nom,
            Boolean active,
            Date creationStart,
            Date creationEnd,
            Date updateStart,
            Date updateEnd,
            Integer etat1,
            Integer etat2,
            Boolean isNewVersion,
            Pageable pageable
    );

    ResponseEntity<Page<ProduitsDto>> GetAllActiveFiltredBy(
            String nom,
            String Nomcategorie,
            String Nomplateforme,
            List<String> Nomtechnologies,
            Integer etat,
            Integer etat2,
            Long idUser,
            Pageable pageable
    );

    ResponseEntity<?> GetAllFiltredBy(String nom, int etat, Timestamp DateCreation, Timestamp DateModification, Pageable pageable);

    public ResponseEntity<?> SubmitProduitChanges(String p, MultipartFile vitrine, List<MultipartFile> files, List<String> titles, Long iduser);
}
