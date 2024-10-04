package tn.esprit.PFE.repository;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import tn.esprit.PFE.entities.Categories;
import org.springframework.data.domain.Pageable;
import tn.esprit.PFE.entities.Produits;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Categories,Long> {
    Categories findByNom(String nom);
    List<Categories> findAllByNom(String nom);
    List<Categories> findAllByActiveOrderByNomAsc(Boolean active);
    Page<Categories> findAll(Specification<Categories> produitsSpecification, Pageable pageable);
    List<Categories> findAllByActive(boolean active);
    Page<Categories> findAllByActiveOrderByNomAsc(Boolean active,Pageable pageable);
    @Query("SELECT c, COUNT(p) AS produitCount " +
            "FROM Categories c " +
            "LEFT JOIN c.produits p " +
            "WHERE c.active = true " +
            "GROUP BY c.id " +
            "ORDER BY produitCount DESC")
    List<Categories> findTop6CategoriesWithMostProducts();
    @Query("SELECT COUNT(*) FROM Categories")
    Number GetCountCategoreis();
    List<Categories> findByMeilleur(boolean meilleur);
    @Query("SELECT c FROM Categories c WHERE c.meilleur = true AND c.active = true")
    List<Categories> findByMeilleurTrueAndActiveTrue();

}
