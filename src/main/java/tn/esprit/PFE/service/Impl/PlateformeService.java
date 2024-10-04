package tn.esprit.PFE.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.entities.Categories;
import tn.esprit.PFE.entities.Plateforme;
import tn.esprit.PFE.payload.response.MessageResponse;
import tn.esprit.PFE.repository.PlateformeRepository;
import tn.esprit.PFE.service.IPlateforme;
import tn.esprit.PFE.utils.SaveFiles;

import javax.persistence.criteria.Predicate;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlateformeService implements IPlateforme {
    @Autowired
    PlateformeRepository plateformeRepository;

    @Value("${app.FileLocation}")
    private String Location;
    @Autowired
    private SaveFiles saveFiles;

    @Override
    public ResponseEntity<?> AddPlateforme(Plateforme p) {
        Plateforme plateforme=plateformeRepository.findByNom(p.getNom());
        if(plateforme==null){
            plateformeRepository.save(p);
            return new ResponseEntity<>(p, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Nom Plateforme Exsite"));

    }

    @Override
    public ResponseEntity<?> ModifierPlateforme(Plateforme p) {
        Plateforme plateforme=plateformeRepository.findById(p.getId()).orElse(null);
        if(plateforme!=null){
            plateforme.setImage_url(p.getImage_url() == null ? plateforme.getImage_url() : p.getImage_url());
            plateforme.setNom(p.getNom() == null ? plateforme.getNom() : p.getNom());
            plateforme.setActive(p.isActive());
            plateformeRepository.save(plateforme);
            return new ResponseEntity<>(plateforme, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Plateforme n'exsite pas"));
    }



    @Override
    public void ChangeActive(Long id) {
        Plateforme plateforme=plateformeRepository.findById(id).orElse(null);
        if(plateforme!=null){
            plateforme.setActive(!plateforme.isActive());
            plateformeRepository.save(plateforme);
        }
    }


    @Override
    public ResponseEntity<?> GetPlateforme(Long id) {
        return new ResponseEntity<>(plateformeRepository.findById(id).orElse(null), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> EditFile(MultipartFile file, String name, Long id) {
        Plateforme p = plateformeRepository.findById(id).orElse(null);
        if (p != null) {
            saveFiles.DeleteFile(p.getImage_url());
            return saveFiles.SaveFile(file, name);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Plateforme n'existe pas"));
    }

    @Override
    public ResponseEntity<?> GetAllByActiveOrderByNom() {
        return new ResponseEntity<>(plateformeRepository.findAllByActiveOrderByNomAsc(true), HttpStatus.OK);
    }



    @Override
    public ResponseEntity<Page<Plateforme>> GetAllFiltredBy(
            String nom,
            Boolean active,
            Pageable pageable
    ) {
        Page<Plateforme> plateformePage = plateformeRepository.findAll((Specification<Plateforme>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nom != null && !nom.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), "%" + nom.toLowerCase() + "%"));
            }

            if (active != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);


        return new ResponseEntity<>(plateformePage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> GetTop5Plateformes() {
        List<Plateforme> results = plateformeRepository.findAllByActive(true);
        if(results.size()>=5){
            results = results.subList(0,5);
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    public Number GetNumberPlatforme() {
        return plateformeRepository.GetNumberPlatforme();    
    }


    public ResponseEntity<?> SaveFile(MultipartFile file, String name) {
        try {
            Path root = Paths.get(Location);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            String filename = name + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), root.resolve(filename));
            return new ResponseEntity<>(new MessageResponse(filename), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("Could not save file: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void DeleteFile(String filename) {
        try {
            Path file = Paths.get(Location).resolve(filename);
            Files.deleteIfExists(file);
        } catch (Exception e) {
            // Handle deletion error
        }
    }

}
