package tn.esprit.PFE.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;

import tn.esprit.PFE.entities.Produits;

import java.util.List;
import java.util.Optional;

// @Repository
public interface ProduitsRepository extends JpaRepository<Produits,Long>, JpaSpecificationExecutor<Produits> {
    @Query("SELECT COUNT(*) FROM Produits")
    float getNumberProducts();
    @Query("SELECT COUNT(*) FROM Produits WHERE active = 1")
    float getNumberProductsActive();
    @Query("SELECT COUNT(*) FROM Produits WHERE active = 0")
    float getNumberProductsInactive();
    @Query("SELECT COUNT(*) FROM Produits WHERE etat = 0")
    float getNumberProductsDemande();
    @Query("SELECT COUNT(*) FROM Produits WHERE etat = 1")
    float getNumberProductsRefuser();
    @Query("SELECT COUNT(*) FROM Produits WHERE etat = 3")
    float getNumberProductsArchiver();
    @Query("SELECT COUNT(*) FROM Produits WHERE etat = 2")
    float getNumberProductsAcepter();
    @Query("SELECT COUNT(*) FROM Produits WHERE etat = 4")
    float getNumberProductsSupprimer();
    Page<Produits> findAll(Pageable pageable);
    Page<Produits> findAllByEtat(int etat,Pageable pageable);
    Page<Produits> findAllByEtatBetween(int etat1,int etat2,Pageable pageable);
    Page<Produits> findAll(Specification<Produits> produitsSpecification, Pageable pageable);
    List<Produits> findByOriginalProduitIdAndIsNewVersion(Long originalProduitId, boolean isNewVersion);
    List<Produits> findByUserId(Long userId);

    @Query("SELECT p FROM Produits p WHERE p.id = :originalProduitId")
    Optional<Produits> findOriginalProduitById(Long originalProduitId);
}
