package tn.esprit.PFE.service.Impl;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.dto.ProduitsDto;
import tn.esprit.PFE.entities.Categories;
import tn.esprit.PFE.entities.Produits;
import tn.esprit.PFE.entities.ProduitsAssets;
import tn.esprit.PFE.entities.User;
import tn.esprit.PFE.repository.FavorisRepository;
import tn.esprit.PFE.repository.ProduitsRepository;
import tn.esprit.PFE.repository.UserRepository;
import tn.esprit.PFE.service.IProduits;
import org.springframework.data.domain.Pageable;
import tn.esprit.PFE.utils.SaveFiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.*;

@Service
public class ProduitsService implements IProduits {
    @Autowired
    ProduitsRepository produitsRepository;
    @Autowired
    ProduitsAssetsService produitsAssetsService;
    @Autowired
    CategoriesService categoriesService;
    @Autowired
    UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private FavorisRepository favorisRepository;
    @Autowired
    private SaveFiles saveFiles;


    @Override
    public ResponseEntity<?> AddProduit(String p,MultipartFile vitrine, List<MultipartFile> files, List<String> titles,Long iduser) {
        Produits produits1=new Produits();
        User user=userRepository.findById(iduser).orElse(null);

        Produits produits = new Gson().fromJson(p, Produits.class);
        produits1=produitsRepository.save(produits1);
        produits.setUser(user);
        produits.setId(produits1.getId());
        produits.setActive(true);
        produits.setId_user(iduser);
        produits.setEtat(0);

        ResponseEntity<?> response= saveFiles.SaveFile(vitrine,"Produits/"+String.valueOf(produits.getId())+"_"+produits.getNom());
        Object Body=response.getBody();
        if (Body != null) {
            Map<String, String> responseMap = (Map<String, String>) Body;
            String path = responseMap.get("path");
            produits.setImage_vitrine(path);
        }
        produits=produitsRepository.save(produits);
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String titre = titles.get(i);
            ProduitsAssets produitsAssets=new ProduitsAssets();
            produitsAssets.setNom(titre);
            produitsAssets.setType(file.getContentType());
            ResponseEntity<?> responseEntity= saveFiles.SaveFile(file,"Produits/"+String.valueOf(produits.getId())+"_"+produits.getNom());
            Object responseBody=responseEntity.getBody();
            if (responseBody != null) {
                Map<String, String> responseMap = (Map<String, String>) responseBody;
                String path = responseMap.get("path");
                produitsAssets.setAsset_url(path);
                produitsAssets.setProduits(produits);
                produitsAssetsService.AddAsset(produitsAssets);
            }
        }
        return new ResponseEntity<>(produits, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> AddProduitPerso(String p,Long iduser) {
        Produits produits1=new Produits();
        User user=userRepository.findById(iduser).orElse(null);
        Produits produits = new Gson().fromJson(p, Produits.class);
        produits1=produitsRepository.save(produits1);
        produits.setUser(user);
        produits.setId(produits1.getId());
        produits.setActive(true);
        produits.setId_user(iduser);
        produits.setEtat(4);
        produits=produitsRepository.save(produits);
        return new ResponseEntity<>(produits, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> ModifierProduit(String p, MultipartFile vitrine, List<MultipartFile> files, List<String> titles) {
        Produits produits = new Gson().fromJson(p, Produits.class);
        Produits produits1=produitsRepository.findById(produits.getId()).orElse(null);
        if(produits1!=null){
            produits1.setActive(produits.isActive());
            produits1.setEtat(produits.getEtat());
            produits1.setCategories(produits.getCategories() == null ? produits1.getCategories() : produits.getCategories());
            produits1.setPlateformes(produits.getPlateformes() == null ? produits1.getPlateformes() : produits.getPlateformes());
            produits1.setTechnologies(produits.getTechnologies() == null ? produits1.getTechnologies() : produits.getTechnologies());
            produits1.setNom(produits.getNom() == null ? produits1.getNom() : produits.getNom());
            produits1.setLien(produits.getLien() == null ? produits1.getLien() : produits.getLien());
            produits1.setDescription(produits.getDescription() == null ? produits1.getDescription() : produits.getDescription());
            produits1.setPrix(produits.getPrix() <0 ? produits1.getPrix() : produits.getPrix());
            if(vitrine!=null){
                ResponseEntity<?> response= saveFiles.SaveFile(vitrine,"Produits/"+String.valueOf(produits.getId())+"_"+produits.getNom());
                Object Body=response.getBody();
                if (Body != null) {
                    Map<String, String> responseMap = (Map<String, String>) Body;
                    String path = responseMap.get("path");
                    produits.setImage_vitrine(path);
                }
            }
            produits=produitsRepository.save(produits1);
        }
        if(files !=null){
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                String titre = titles.get(i);
                ProduitsAssets produitsAssets=new ProduitsAssets();
                produitsAssets.setNom(titre);
                produitsAssets.setType(file.getContentType());
                ResponseEntity<?> responseEntity= saveFiles.SaveFile(file,"Produits/"+String.valueOf(produits.getId())+"_"+produits.getNom());
                Object responseBody=responseEntity.getBody();
                if (responseBody != null) {
                    Map<String, String> responseMap = (Map<String, String>) responseBody;
                    String path = responseMap.get("path");
                    produitsAssets.setAsset_url(path);
                    produitsAssets.setProduits(produits1);
                    produitsAssetsService.AddAsset(produitsAssets);
                }
            }
        }

        return new ResponseEntity<>(produits, HttpStatus.OK);
    }

    @Override
    public void ModifyEtat(Long id, int etat) {
        Produits produits=produitsRepository.findById(id).orElse(null);
        if(produits!=null){
            produits.setEtat(etat);
            produitsRepository.save(produits);
        }
    }
    public void AcceptProduct(Long modifiedProductId) {
        Produits modifiedProduct = produitsRepository.findById(modifiedProductId).orElse(null);
        if (modifiedProduct != null) {
            Long originalProductId = modifiedProduct.getOriginalProduitId();

            if (originalProductId != null) {
                // Fetch the original product
                Produits originalProduct = produitsRepository.findById(originalProductId).orElse(null);
                if (originalProduct != null) {
                    // Set the original product's status to 'archivée'
                    originalProduct.setEtat(3); // 3: archivée
                    produitsRepository.save(originalProduct);
                }
            }

            // Set the modified product's status to 'acceptée'
            modifiedProduct.setEtat(2); // 2: acceptée
            produitsRepository.save(modifiedProduct);
        }
    }

    public Produits archiveProduit(Long produitId) {
        Produits produit = produitsRepository.findById(produitId)
                .orElseThrow(() -> new IllegalArgumentException("Produit not found with id: " + produitId));

        produit.setEtat(3);
        return produitsRepository.save(produit);
    }

    @Override
    public ResponseEntity<?> GetProduit(Long id,Long idu) {
        Produits p=produitsRepository.findById(id).orElse(null);
        if(idu!=null){
            if(favorisRepository.findByUserIdAndProduitsId(idu,id)!=null){
                p.setFavoris(true);
            }
        }
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<ProduitsDto>> GetAllFiltredBy(
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
    ) {
        List<ProduitsDto> listdto = new ArrayList<>();

        Page<Produits> produitsPage = produitsRepository.findAll((Specification<Produits>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add filters based on the provided criteria
            if (nom != null && !nom.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), "%" + nom.toLowerCase() + "%"));
            }

            if (active != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }

            if (creationStart != null && creationEnd != null) {
                predicates.add(criteriaBuilder.between(root.get("DateCreation"), creationStart, creationEnd));
            }

            if (updateStart != null && updateEnd != null) {
                predicates.add(criteriaBuilder.between(root.get("DateModification"), updateStart, updateEnd));
            }

            if (etat1 != null && etat2 != null) {
                predicates.add(criteriaBuilder.between(root.get("etat"), etat1, etat2));
            }

            if (etat1 != null && etat2 == null) {
                predicates.add(criteriaBuilder.equal(root.get("etat"), etat1));
            }

            // Filter for isNewVersion
            if (isNewVersion != null) {
                predicates.add(criteriaBuilder.equal(root.get("isNewVersion"), isNewVersion));
            } else {
                // Default to false if isNewVersion is not provided
                predicates.add(criteriaBuilder.equal(root.get("isNewVersion"), false));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        for (Produits p : produitsPage.getContent()) {
            listdto.add(new ProduitsDto(p));
        }

        Page<ProduitsDto> produitsDtoPage = new PageImpl<>(listdto, pageable, produitsPage.getTotalElements());
        return new ResponseEntity<>(produitsDtoPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<ProduitsDto>> GetAllFiltredBys(
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
    ) {
        List<ProduitsDto> listdto = new ArrayList<>();

        Page<Produits> produitsPage = produitsRepository.findAll((Specification<Produits>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add filters based on the provided criteria
            if (nom != null && !nom.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), "%" + nom.toLowerCase() + "%"));
            }

            if (active != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }

            if (creationStart != null && creationEnd != null) {
                predicates.add(criteriaBuilder.between(root.get("DateCreation"), creationStart, creationEnd));
            }

            if (updateStart != null && updateEnd != null) {
                predicates.add(criteriaBuilder.between(root.get("DateModification"), updateStart, updateEnd));
            }

            if (etat1 != null && etat2 != null) {
                predicates.add(criteriaBuilder.between(root.get("etat"), etat1, etat2));
            }

            if (etat1 != null && etat2 == null) {
                predicates.add(criteriaBuilder.equal(root.get("etat"), etat1));
            }

            // Filter for isNewVersion
            if (isNewVersion != null) {
                predicates.add(criteriaBuilder.equal(root.get("isNewVersion"), isNewVersion));
            } else {
                // Default to false if isNewVersion is not provided
                predicates.add(criteriaBuilder.equal(root.get("isNewVersion"), true));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        for (Produits p : produitsPage.getContent()) {
            listdto.add(new ProduitsDto(p));
        }

        Page<ProduitsDto> produitsDtoPage = new PageImpl<>(listdto, pageable, produitsPage.getTotalElements());
        return new ResponseEntity<>(produitsDtoPage, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Page<ProduitsDto>> GetAllActiveFiltredBy(
            String nom,
            String Nomcategorie,
            String Nomplateforme,
            List<String> Nomtechnologies,
            Integer etat,
            Integer etat2,
            Long idUser,
            Pageable pageable
    ) {
        List<ProduitsDto> listdto = new ArrayList<>();

        Page<Produits> produitsPage = produitsRepository.findAll((Specification<Produits>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Add filters based on the provided criteria
            if (nom != null && !nom.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), "%" + nom.toLowerCase() + "%"));
            }
            // Add filter for category name
            if (Nomcategorie != null && !Nomcategorie.isEmpty()) {
                Join<Produits, Categories> categoryJoin = root.join("categories");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("nom")), "%" + Nomcategorie.toLowerCase() + "%"));
            }

            if (Nomplateforme != null && !Nomplateforme.isEmpty()) {
                Join<Produits, Categories> categoryJoin = root.join("plateformes");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("nom")), "%" + Nomplateforme.toLowerCase() + "%"));
            }

            if (Nomtechnologies != null && !Nomtechnologies.isEmpty()) {
                Join<Produits, Categories> categoryJoin = root.join("technologies");
                for (String nomt:Nomtechnologies) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("nom")), "%" + nomt + "%"));
                }
            }
            if (idUser != null ) {
                Join<Produits, User> categoryJoin = root.join("user");
                    predicates.add(criteriaBuilder.equal(categoryJoin.get("id"), idUser));
            }

            if (etat != null && etat2 != null) {
                predicates.add(criteriaBuilder.between(root.get("etat"), etat, etat2));
            }

            if (etat != null && etat2 == null) {
                predicates.add(criteriaBuilder.equal(root.get("etat"), etat));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        for (Produits p : produitsPage.getContent()) {
            listdto.add(new ProduitsDto(p));
        }

        Page<ProduitsDto> produitsDtoPage = new PageImpl<>(listdto, pageable, produitsPage.getTotalElements());
        return new ResponseEntity<>(produitsDtoPage, HttpStatus.OK);
    }




    @Override
    public ResponseEntity<?> GetAllFiltredBy(String nom, int etat, Timestamp DateCreation, Timestamp DateModification,Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produits> criteriaQuery = criteriaBuilder.createQuery(Produits.class);
        Root<Produits> root = criteriaQuery.from(Produits.class);
        List<Predicate> predicates = new ArrayList<>();
        if (nom != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), "%" + nom.toLowerCase() + "%"));
        }


        return new ResponseEntity<>(predicates, HttpStatus.OK);
    }
    // getNumberProducts
    @Override
    public float getNumberProducts() {
        return produitsRepository.getNumberProducts();
    }
    // getNumberProductsActive
    @Override
    public float getNumberProductsActive() {
        return produitsRepository.getNumberProductsActive();
    }
    // getNumberProductsInactive
    @Override
    public float getNumberProductsInactive() {
        return produitsRepository.getNumberProductsInactive();
    }

    @Override
    public float getNumberProductsDemande() {
        return produitsRepository.getNumberProductsDemande();
    }

    @Override
    public float getNumberProductsRefuser() {
        return produitsRepository.getNumberProductsRefuser();
    }

    @Override
    public float getNumberProductsArchiver() {
        return produitsRepository.getNumberProductsArchiver();
    }

    @Override
    public float getNumberProductsAcepter() {
        return produitsRepository.getNumberProductsAcepter();
    }

    @Override
    public float getNumberProductsSupprimer() {
        return produitsRepository.getNumberProductsSupprimer();
    }

    @Override
    public ResponseEntity<?> SubmitProduitChanges(String p, MultipartFile vitrine, List<MultipartFile> files, List<String> titles, Long iduser) {
        Produits newProduitVersion = new Gson().fromJson(p, Produits.class);

        // Validate that the ID is not null
        if (newProduitVersion.getId() == null) {
            return new ResponseEntity<>("Produit ID must not be null!", HttpStatus.BAD_REQUEST);
        }

        Produits existingProduit = produitsRepository.findById(newProduitVersion.getId()).orElse(null);

        if (existingProduit == null || !existingProduit.getId_user().equals(iduser)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Create a new version of the produit
        Produits produitToSave = new Produits();
        produitToSave.setNom(newProduitVersion.getNom());
        produitToSave.setDescription(newProduitVersion.getDescription());
        produitToSave.setPrix(newProduitVersion.getPrix());
        produitToSave.setLien(newProduitVersion.getLien());
        produitToSave.setId_user(existingProduit.getId_user());
        produitToSave.setImage_vitrine(existingProduit.getImage_vitrine());
        produitToSave.setEtat(0); // Pending approval
        produitToSave.setActive(false);
        produitToSave.setOriginalProduitId(existingProduit.getId());
        produitToSave.setNewVersion(true);
        produitToSave.setUser(existingProduit.getUser());

        // Create new instances for the collections
        produitToSave.setCategories(new ArrayList<>(existingProduit.getCategories()));
        produitToSave.setPlateformes(new ArrayList<>(existingProduit.getPlateformes()));
        produitToSave.setTechnologies(new ArrayList<>(existingProduit.getTechnologies()));

        // Handle vitrine image
        if (vitrine != null && !vitrine.isEmpty()) {
            ResponseEntity<?> response = saveFiles.SaveFile(vitrine, "Produits/" + produitToSave.getId() + "_" + produitToSave.getNom());
            if (response.getBody() != null) {
                Map<String, String> responseMap = (Map<String, String>) response.getBody();
                produitToSave.setImage_vitrine(responseMap.get("path"));
            }
        }

        // Save the new version
        produitToSave = produitsRepository.save(produitToSave);

        // Handle additional files and titles
        if (files != null && !files.isEmpty()) {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                String titre = titles.get(i);
                ProduitsAssets produitsAssets = new ProduitsAssets();
                produitsAssets.setNom(titre);
                produitsAssets.setType(file.getContentType());
                ResponseEntity<?> responseEntity = saveFiles.SaveFile(file, "Produits/" + produitToSave.getId() + "_" + produitToSave.getNom());
                if (responseEntity.getBody() != null) {
                    Map<String, String> responseMap = (Map<String, String>) responseEntity.getBody();
                    produitsAssets.setAsset_url(responseMap.get("path"));
                    produitsAssets.setProduits(produitToSave);
                    produitsAssetsService.AddAsset(produitsAssets);
                }
            }
        }

        return new ResponseEntity<>(produitToSave, HttpStatus.OK);
    }



    public ResponseEntity<?> ApproveOrRejectProduitVersion(Long id, boolean approve) {
        Produits newVersion = produitsRepository.findById(id).orElse(null);

        if (newVersion == null || !newVersion.isNewVersion()) {
            return new ResponseEntity<>("Product version not found or not a new version!", HttpStatus.NOT_FOUND);
        }

        if (approve) {
            Produits originalProduit = produitsRepository.findById(newVersion.getOriginalProduitId()).orElse(null);
            if (originalProduit != null) {
                originalProduit.setEtat(3); // Archived
                originalProduit.setActive(false);
                produitsRepository.save(originalProduit);
            }
            newVersion.setEtat(2); // Approved
            newVersion.setActive(true);
        } else {
            newVersion.setEtat(1); // Rejected
        }

        newVersion.setNewVersion(false); // Mark as no longer a new version
        produitsRepository.save(newVersion);

        return new ResponseEntity<>(newVersion, HttpStatus.OK);
    }


    public List<Produits> getProductsByUserId(Long userId) {
        return produitsRepository.findByUserId(userId);
    }


    public ResponseEntity<?> getOriginalProduit(Long modifiedProduitId) {
        Optional<Produits> modifiedProduit = produitsRepository.findById(modifiedProduitId);

        if (modifiedProduit.isPresent()) {
            Long originalProduitId = modifiedProduit.get().getOriginalProduitId();
            if (originalProduitId != null) {
                Optional<Produits> originalProduit = produitsRepository.findOriginalProduitById(originalProduitId);

                if (originalProduit.isPresent()) {
                    return new ResponseEntity<>(originalProduit.get(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Original product not found", HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>("This product is not a modified version", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }
}





