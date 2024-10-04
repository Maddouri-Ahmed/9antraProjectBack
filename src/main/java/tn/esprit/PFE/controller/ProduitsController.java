package tn.esprit.PFE.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.entities.Produits;
import tn.esprit.PFE.repository.ProduitsRepository;
import tn.esprit.PFE.service.Impl.ProduitsService;

import org.springframework.data.domain.Pageable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"*"})
@RequestMapping("/api")
@RestController
public class ProduitsController {

    @Autowired
    ProduitsRepository produitsRepository;
    @Autowired
    ProduitsService     produitsService;
    @PostMapping(value = "/AddProduit")
    public ResponseEntity<?> Addproduit(@RequestParam("assets") List<MultipartFile> files,
                                  @RequestParam("titles") List<String> titles,
                                  @RequestParam("vitrine") MultipartFile vitrine,
                                  @RequestParam("produit") String produit,
                                  @RequestParam("iduser") Long iduser
    ) {
        return produitsService.AddProduit(produit,vitrine,files,titles,iduser);
    }
    @PostMapping(value = "/SubmitProduitChanges", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitProduitChanges(
            @RequestParam("produit") String produitJson,
            @RequestParam("vitrine") MultipartFile vitrine,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("titles") List<String> titles,
            @RequestParam("iduser") Long iduser) {

        // Convert produitJson to Produits object
        Produits newProduitVersion;
        try {
            newProduitVersion = new Gson().fromJson(produitJson, Produits.class);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid produit JSON format!", HttpStatus.BAD_REQUEST);
        }

        // Check if ID is null
        if (newProduitVersion.getId() == null) {
            return new ResponseEntity<>("Produit ID must not be null!", HttpStatus.BAD_REQUEST);
        }

        // Call your service method
        return produitsService.SubmitProduitChanges(produitJson, vitrine, files, titles, iduser);
    }





    @PostMapping(value = "/ApproveOrRejectProduitVersion")
    public ResponseEntity<?> approveOrRejectProduitVersion(@RequestParam("id") Long id, @RequestParam("approve") boolean approve) {
        return produitsService.ApproveOrRejectProduitVersion(id, approve);
    }


    @PostMapping(value = "/AddProduitPerso")
    public ResponseEntity<?> AddproduitPerso(@RequestParam("produit") String produit,
                                             @RequestParam("iduser") Long iduser
    ) {
        return produitsService.AddProduitPerso(produit,iduser);
    }

    @PostMapping(value = "/ModifyProduit")
    public ResponseEntity<?> ModifierProduit(@RequestParam(value = "assets",required = false) List<MultipartFile> files,
                                     @RequestParam(value = "titles" , required = false) List<String> titles,
                                     @RequestParam(value = "vitrine", required = false) MultipartFile vitrine,
                                     @RequestParam("produit") String produit
    ) {
        if(vitrine !=null){
            return produitsService.ModifierProduit(produit,vitrine,files,titles);
        }
        return produitsService.ModifierProduit(produit,null,files,titles);
    }

    @GetMapping(value = "/GetProduitById")
    public ResponseEntity<?> GetById( @RequestParam("id") Long id,
                                      @RequestParam(value = "idu",required = false) Long idu) {
        return produitsService.GetProduit(id,idu);
    }
    @GetMapping("/originals/{modifiedProduitId}")
    public ResponseEntity<?> getOriginalProduits(@PathVariable Long modifiedProduitId) {
        return produitsService.getOriginalProduit(modifiedProduitId);
    }

    @GetMapping(value = "/GetAllByFilter")
    public ResponseEntity<?> GetAllFiltredBy(
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "creationStart", required = false) Date creationStart,
            @RequestParam(value = "creationEnd", required = false) Date creationEnd,
            @RequestParam(value = "updateStart", required = false) Date updateStart,
            @RequestParam(value = "updateEnd", required = false) Date updateEnd,
            @RequestParam(value = "etat1", required = false) Integer etat1,
            @RequestParam(value = "etat2", required = false) Integer etat2,
            @RequestParam(value = "isNewVersion", defaultValue = "false") Boolean isNewVersion, // Added parameter
            Pageable pageable
    ) {
        return produitsService.GetAllFiltredBy(nom, active, creationStart, creationEnd, updateStart, updateEnd, etat1, etat2, isNewVersion, pageable);
    }

    @GetMapping(value = "/GetAllByFilters")
    public ResponseEntity<?> GetAllFiltredBys(
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "creationStart", required = false) Date creationStart,
            @RequestParam(value = "creationEnd", required = false) Date creationEnd,
            @RequestParam(value = "updateStart", required = false) Date updateStart,
            @RequestParam(value = "updateEnd", required = false) Date updateEnd,
            @RequestParam(value = "etat1", required = false) Integer etat1,
            @RequestParam(value = "etat2", required = false) Integer etat2,
            @RequestParam(value = "isNewVersion", defaultValue = "true") Boolean isNewVersion, // Added parameter
            Pageable pageable
    ) {
        return produitsService.GetAllFiltredBys(nom, active, creationStart, creationEnd, updateStart, updateEnd, etat1, etat2, isNewVersion, pageable);
    }


    @PostMapping(value = "/ModifyEtatProduit")
    public void ModifyEtatProduit(@RequestParam("id") Long id,@RequestParam("etat") int etat) {
        produitsService.ModifyEtat(id,etat);
    }

    @PostMapping("/acceptProduct")
    public void AcceptProduct(@RequestParam("id") Long modifiedProductId) {
        produitsService.AcceptProduct(modifiedProductId);
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<Produits> archiveProduit(@PathVariable Long id) {
        try {
            Produits updatedProduit = produitsService.archiveProduit(id);
            return ResponseEntity.ok(updatedProduit);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/GetAllActiveByFilter")
    public ResponseEntity<?> GetAllActiveFiltredBy( @RequestParam(value = "nom",required = false) String nom,
                                                    @RequestParam(value = "Nomcategorie",required = false) String Nomcategorie,
                                                    @RequestParam(value = "Nomplateforme",required = false) String Nomplateforme,
                                                    @RequestParam(value = "Nomtechnologie",required = false) List<String> Nomtechnologie,
                                                    @RequestParam(value = "etat",required = false) Integer etat,
                                                    @RequestParam(value = "etat2",required = false) Integer etat2,
                                                    @RequestParam(value = "idUser",required = false) Long idUser,
                                              Pageable pageable
    ) {
        return produitsService.GetAllActiveFiltredBy(nom,Nomcategorie,Nomplateforme,Nomtechnologie,etat,etat2,idUser, pageable);
    }
    // GetNumberProducts
    @GetMapping(value = "/GetNumberProducts")
    public float getNumberProducts() {
        return produitsService.getNumberProducts();
    }
    // GetNumberProductsActive
    @GetMapping(value = "/GetNumberProductsActive")
    public float getNumberProductsActive() {
        return produitsService.getNumberProductsActive();
    }
    // GetNumberProductsInactive
    @GetMapping(value = "/GetNumberProductsInactive")
    public float getNumberProductsInactive() {
        return produitsService.getNumberProductsInactive();
    }

    // GetNumberProductsDemande
    @GetMapping(value = "/GetNumberProductsDemande")
    public float getNumberProductsDemande() {
        return produitsService.getNumberProductsDemande();
    }
    // GetNumberProductsRefuser
    @GetMapping(value = "/GetNumberProductsRefuser")
    public float getNumberProductsRefuser() {
        return produitsService.getNumberProductsRefuser();
    }
    // GetNumberProductsArchiver
    @GetMapping(value = "/GetNumberProductsArchiver")
    public float getNumberProductsArchiver() {
        return produitsService.getNumberProductsArchiver();
    }
    // GetNumberProductsAcepter
    @GetMapping(value = "/GetNumberProductsAcepter")
    public float getNumberProductsAcepter() {
        return produitsService.getNumberProductsAcepter();
    }
    // GetNumberProductsSupprimer
    @GetMapping(value = "/GetNumberProductsSupprimer")
    public float getNumberProductsSupprimer() {
        return produitsService.getNumberProductsSupprimer();
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Produits>> getProductsByUserId(@PathVariable Long userId) {
        List<Produits> products = produitsService.getProductsByUserId(userId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/original/{modifiedProduitId}")
    public ResponseEntity<?> getOriginalProduit(@PathVariable Long modifiedProduitId) {
        Optional<Produits> modifiedProduit = produitsRepository.findById(modifiedProduitId);

        if (modifiedProduit.isPresent()) {
            Long originalProduitId = modifiedProduit.get().getOriginalProduitId();
            if (originalProduitId != null) {
                return new ResponseEntity<>(originalProduitId, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("This product is not a modified version", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

}
