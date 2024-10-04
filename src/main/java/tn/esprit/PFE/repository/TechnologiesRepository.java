package tn.esprit.PFE.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tn.esprit.PFE.entities.Categories;
import tn.esprit.PFE.entities.Plateforme;
import tn.esprit.PFE.entities.Technologies;

import java.util.List;

public interface TechnologiesRepository extends JpaRepository<Technologies,Long> {
    Technologies findByNom(String nom);
    Page<Technologies> findByActive(Boolean active, Pageable pageable);
    List<Technologies> findAllByActiveOrderByNomAsc(Boolean active);
    Page<Technologies> findAllByNomContains(String nom,Pageable pageable);
    Page<Technologies> findAllByActiveAndNomContains(boolean active,String nom,Pageable pageable);
    Page<Technologies> findByActiveOrderByIdDesc(Boolean active,Pageable pageable);
    List<Technologies> findAllByActive(boolean active);
    Page<Technologies> findAll(Specification<Technologies> technologiesSpecification, Pageable pageable);
    @Query("SELECT COUNT(*) FROM Technologies")
    Number GetNumberTechnologies();
}
