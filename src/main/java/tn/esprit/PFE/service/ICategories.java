package tn.esprit.PFE.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.entities.Categories;

public interface ICategories {
     ResponseEntity<?> AddCategories(Categories c);
     ResponseEntity<?> ModifierCategories(Categories c);
     void ChangeActive(Long id);
     ResponseEntity<?> GetCategorie(Long id);
     ResponseEntity<?> SaveFile(MultipartFile file,String name);
     ResponseEntity<?> EditFile(MultipartFile file,String name,Long id);
     void DeleteFile(String path);
     ResponseEntity<?> GetAllByActiveOrderByNom(Pageable pageable);

     ResponseEntity<Page<Categories>> GetAllFiltredBy(
             String nom,
             Boolean active,
             Pageable pageable
     );
     ResponseEntity<?> GetTop5Categoreis();
     Number GetCountCategoreis();
}
