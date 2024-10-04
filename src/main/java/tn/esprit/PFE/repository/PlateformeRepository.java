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

public interface PlateformeRepository extends JpaRepository<Plateforme,Long> {
    Plateforme findByNom(String nom);
    Page<Plateforme> findByActive(Boolean active, Pageable pageable);
    List<Plateforme> findAllByActiveOrderByNomAsc(Boolean active);
    Page<Plateforme> findAllByNomContains(String nom,Pageable pageable);
    Page<Plateforme> findAllByActiveAndNomContains(boolean active,String nom,Pageable pageable);
    Page<Plateforme> findByActiveOrderByIdDesc(Boolean active,Pageable pageable);
    List<Plateforme> findAllByActive(boolean active);
    Page<Plateforme> findAll(Specification<Plateforme> plateformeSpecification, Pageable pageable);
    @Query("SELECT COUNT(*) FROM Plateforme")
    Number GetNumberPlatforme();
}
