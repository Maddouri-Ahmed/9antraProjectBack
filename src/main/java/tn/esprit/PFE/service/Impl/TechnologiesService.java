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
import tn.esprit.PFE.entities.TechnologieType;
import tn.esprit.PFE.entities.Technologies;
import tn.esprit.PFE.payload.response.MessageResponse;
import tn.esprit.PFE.repository.TechnologiesRepository;
import tn.esprit.PFE.service.ITechnologies;
import tn.esprit.PFE.utils.SaveFiles;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


@Service
public class TechnologiesService implements ITechnologies {
    @Autowired
    TechnologiesRepository technologiesRepository;
    @Autowired
    private SaveFiles saveFiles;


    @Value("${app.FileLocation}")
    private String Location;

    @Override
    public ResponseEntity<?> AddTechnologie(Technologies t) {
        // Trim and fetch existing technology by name
        t.setNom(t.getNom().trim());
        Technologies existingTechnology = technologiesRepository.findByNom(t.getNom());

        // Check if technology with the same name exists
        if (existingTechnology != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Nom Technologie Exsite"));
        }

        // Validate and set the technology type
        if (t.getType() != null) {
            try {
                // Attempt to set the type from the provided value
                t.setType(TechnologieType.valueOf(t.getType().name()));
            } catch (IllegalArgumentException e) {
                // Return error if type is invalid
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid Technologie Type"));
            }
        } else {
            // Handle the case where type is null
            return ResponseEntity.badRequest().body(new MessageResponse("Technologie Type is required"));
        }

        // Save the new technology if no existing entry was found
        technologiesRepository.save(t);
        return new ResponseEntity<>(t, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> ModifierTechnologie(Technologies t) {
        Technologies technologies=technologiesRepository.findById(t.getId()).orElse(null);
        if(technologies!=null){
            technologies.setImage_url(t.getImage_url() == null ? technologies.getImage_url() : t.getImage_url());
            technologies.setNom(t.getNom() == null ? technologies.getNom() : t.getNom());
            technologies.setActive(t.isActive());
            technologiesRepository.save(technologies);
            return new ResponseEntity<>(technologies, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Technologie n'exsite pas"));

    }

    @Override
    public void ChangeActive(Long id) {
        Technologies technologies=technologiesRepository.findById(id).orElse(null);
        if(technologies!=null){
            technologies.setActive(!technologies.isActive());
            technologiesRepository.save(technologies);
        }
    }


    @Override
    public ResponseEntity<?> GetTechnologie(Long id) {
        return new ResponseEntity<>(technologiesRepository.findById(id).orElse(null), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> EditFile(MultipartFile file, String name, Long id) {
        Technologies t = technologiesRepository.findById(id).orElse(null);
        if (t != null) {
            saveFiles.DeleteFile(t.getImage_url());
            return saveFiles.SaveFile(file, name);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Technologie n'existe pas"));
    }

    @Override
    public ResponseEntity<?> GetAllByActiveOrderByNom() {
        return new ResponseEntity<>(technologiesRepository.findAllByActiveOrderByNomAsc(true), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Page<Technologies>> GetAllFiltredBy(
            String nom,
            Boolean active,
            Pageable pageable
    ) {
        Page<Technologies> TechnologiesPage = technologiesRepository.findAll((Specification<Technologies>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nom != null && !nom.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nom")), "%" + nom.toLowerCase() + "%"));
            }

            if (active != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);


        return new ResponseEntity<>(TechnologiesPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> GetTopTechnologies() {
        List<Technologies> results = technologiesRepository.findAllByActive(true);
        if(results.size()>=8){
            results = results.subList(0,8);
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @Override
    public Number GetNumberTechnologies() {
        return technologiesRepository.GetNumberTechnologies();
    }
}
