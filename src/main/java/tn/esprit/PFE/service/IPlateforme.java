package tn.esprit.PFE.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.entities.Plateforme;

public interface IPlateforme {
    ResponseEntity<?> AddPlateforme(Plateforme p);
    ResponseEntity<?> ModifierPlateforme(Plateforme p);
    void ChangeActive(Long id);
    ResponseEntity<?> GetPlateforme(Long id);
    ResponseEntity<?> EditFile(MultipartFile file, String name, Long id);
    ResponseEntity<?> GetAllByActiveOrderByNom();

    ResponseEntity<Page<Plateforme>> GetAllFiltredBy(
            String nom,
            Boolean active,
            Pageable pageable
    );

    ResponseEntity<?> GetTop5Plateformes();
}
