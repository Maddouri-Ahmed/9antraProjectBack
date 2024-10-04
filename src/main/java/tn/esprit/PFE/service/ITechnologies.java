package tn.esprit.PFE.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.entities.Technologies;

public interface ITechnologies {
    ResponseEntity<?> AddTechnologie(Technologies t);
    ResponseEntity<?> ModifierTechnologie(Technologies t);
    void ChangeActive(Long id);
    ResponseEntity<?> GetTechnologie(Long id);
    ResponseEntity<?> EditFile(MultipartFile file, String name, Long id);
    ResponseEntity<?> GetAllByActiveOrderByNom();

    ResponseEntity<Page<Technologies>> GetAllFiltredBy(
            String nom,
            Boolean active,
            Pageable pageable
    );

    ResponseEntity<?> GetTopTechnologies();

    Number GetNumberTechnologies();
}
