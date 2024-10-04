package tn.esprit.PFE.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.PFE.entities.Categories;
import tn.esprit.PFE.entities.ProduitDemande;


public interface IDemandeRepository extends JpaRepository<ProduitDemande, Long> {

        @Query("SELECT COUNT(id) FROM ProduitDemande")
        float getNumberDemande();
        @Query("SELECT COUNT(id) FROM ProduitDemande WHERE etat = 0")
        float getNumberDemandeEnAttente();
        @Query("SELECT COUNT(id) FROM ProduitDemande WHERE etat = 1")
        float getNumberDemandeRefuser();
        @Query("SELECT COUNT(id) FROM ProduitDemande WHERE etat = 2")
        float getNumberDemandeAccepter();
        // @Query("SELECT SUM(P.prix) FROM Produits P JOIN ProduitDemande D ON D.produits_id = P.id WHERE D.etat = 2")
        @Query("SELECT SUM(P.prix) FROM Produits P JOIN ProduitDemande D ON D.produits = P.id WHERE D.etat = 2")
        // float getChiffreAffaireTotalProduitVendu();
        // @Query("SELECT SUM(P.prix) FROM Produits P JOIN P.produitDemande D WHERE D.etat = 2")
        float getChiffreAffaireTotalProduitVendu();
        Page<ProduitDemande> findAll(Specification<ProduitDemande> produitsSpecification, Pageable pageable);
        ProduitDemande findByEtatAndUserIdAndProduitsId(int etat,Long idu,Long idp);





        }