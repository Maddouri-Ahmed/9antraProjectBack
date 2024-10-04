package tn.esprit.PFE.service.Impl;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.dto.ProduitsDto;
import tn.esprit.PFE.entities.Categories;
import tn.esprit.PFE.payload.response.MessageResponse;
import tn.esprit.PFE.repository.CategoriesRepository;
import tn.esprit.PFE.service.ICategories;
import tn.esprit.PFE.utils.SaveFiles;

import javax.persistence.criteria.Predicate;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class CategoriesService implements ICategories {
    @Autowired
    CategoriesRepository categoriesRepository;

    @Value("${app.FileLocation}")
    private String Location;

    @Autowired
    private SaveFiles saveFiles;

    @Override
    public ResponseEntity<?> AddCategories(Categories c) {
        Categories categories = categoriesRepository.findByNom(c.getNom());
        if (categories == null) {
            categoriesRepository.save(c);
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Nom Categorie '" + c.getNom() + "' Existe"));
    }

    @Override
    public ResponseEntity<?> ModifierCategories(Categories c) {
        Categories categories = categoriesRepository.findById(c.getId()).orElse(null);
        if (categories != null) {
            List<Categories> categoriesList = categoriesRepository.findAllByNom(c.getNom());
            if ((categoriesList.isEmpty() && c.getNom() != categories.getNom()) || c.getNom().equals(categories.getNom())) {
                categories.setImage_url(c.getImage_url() == null ? categories.getImage_url() : c.getImage_url());
                categories.setDescription(c.getDescription() == null ? categories.getDescription() : c.getDescription());
                categories.setNom(c.getNom() == null ? categories.getNom() : c.getNom());
                categories.setActive(c.isActive());
                categoriesRepository.save(categories);
                return new ResponseEntity<>(c, HttpStatus.OK);
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("Nom Categorie '" + c.getNom() + "' Existe"));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Categorie n'exsite pas"));
    }

    @Override
    public void ChangeActive(Long id) {
        Categories categories = categoriesRepository.findById(id).orElse(null);
        if (categories != null) {
           categories.setActive(!categories.isActive());
            categoriesRepository.save(categories);
        }
    }


    @Override
    public ResponseEntity<?> GetCategorie(Long id) {
        return new ResponseEntity<>(categoriesRepository.findById(id).orElse(null), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> SaveFile(MultipartFile file, String name) {
       return saveFiles.SaveFile(file,name);
    }

    @Override
    public ResponseEntity<?> EditFile(MultipartFile file, String name, Long id) {
        Categories c = categoriesRepository.findById(id).orElse(null);
        if (c != null) {
            this.DeleteFile(c.getImage_url());
            return this.SaveFile(file, name);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Categorie n'existe pas"));
    }


    public void DeleteFile(String path){
       saveFiles.DeleteFile(path);
    }



    @Override
    public ResponseEntity<?> GetAllByActiveOrderByNom(Pageable pageable) {
        if(pageable !=null){
            Page<Categories> CategoriesPage=categoriesRepository.findAllByActiveOrderByNomAsc(true,pageable);
            return new ResponseEntity<>(CategoriesPage, HttpStatus.OK);
        }
        return new ResponseEntity<>(categoriesRepository.findAllByActiveOrderByNomAsc(true), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Page<Categories>> GetAllFiltredBy(
            String nom,
            Boolean active,
            Pageable pageable
    ) {
        Page<Categories> CategoriesPage = categoriesRepository.findAll((Specification<Categories>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nom != null && !nom.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), "%" + nom.toLowerCase() + "%"));
            }

            if (active != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);


        return new ResponseEntity<>(CategoriesPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> GetTop5Categoreis() {
        List<Categories> results = categoriesRepository.findTop6CategoriesWithMostProducts();
        if(results.size()>=5){
            results = results.subList(0,5);
        }
        if(results==null){
            results=categoriesRepository.findAllByActive(true);
            results = results.subList(0,5);
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @Override
    public Number GetCountCategoreis() {
        return categoriesRepository.GetCountCategoreis();
    }


    public void setMeilleur(Long id, boolean isMeilleur) {
        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setMeilleur(isMeilleur);
        categoriesRepository.save(category);
    }
    public Categories getCategory(Long id) {
        return categoriesRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public List<Categories> getCategoriesMeilleur() {
        return categoriesRepository.findByMeilleurTrueAndActiveTrue();
    }
}
