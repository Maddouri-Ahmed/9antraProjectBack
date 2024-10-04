package tn.esprit.PFE.service;

import tn.esprit.PFE.entities.Categore;
import tn.esprit.PFE.entities.Outil;
import tn.esprit.PFE.entities.catégories;
import tn.esprit.PFE.entities.oldproduit;

import java.util.List;


public interface IProduitservice {
    oldproduit saveProduit(oldproduit produit, Long  id);
    oldproduit saveProduitMobile(oldproduit produit, Long  id);
    oldproduit saveProduitDesigne(oldproduit produit, Long  id);
    oldproduit saveProduitvideo(oldproduit produit, Long  id);
    List<oldproduit>getProduits();
    oldproduit findById(Long id);
    List<oldproduit> findByUser(Long  id);
    List<oldproduit> retrieveProduitByCategore(Categore categore);
    List<oldproduit> retrieveProduitByCatégories(catégories catégories);
    List<oldproduit> retrieveProduitByOutil(Outil outil);
    List<oldproduit> retrieveProduitByOutil1(Outil outil1);
    List<oldproduit> retrieveProduitByOutil2(Outil outil2);
    List<oldproduit> retrieveProduitByOutil3(Outil outil3);
    List<oldproduit> retrieveProduitByOutil4(Outil outil4);
    oldproduit updateProductDesigne(oldproduit productDetails, Long id);
    oldproduit updateProductVideo(oldproduit productDetails, Long id);
    oldproduit updateProductMobile(oldproduit productDetails, Long id);
    oldproduit updateProductWeb(oldproduit productDetails, Long id);

}
