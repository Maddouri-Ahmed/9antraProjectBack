package tn.esprit.PFE.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import tn.esprit.PFE.dto.DemandeProduitDto;
//import tn.esprit.PFE.entities.Notification;
import tn.esprit.PFE.entities.ProduitDemande;
import java.util.List;

public interface DemandeService {


    /**
     * Send notification
     * @param notificationDemande model of notification
     */
    void sendD(ProduitDemande notificationDemande);

    ResponseEntity<?> AddDemande(Long idproduit, Long user_id, String message);

    ProduitDemande save(ProduitDemande Demande ) ;

    ResponseEntity<Page<DemandeProduitDto>> getProduitDemande(String nomProduit, String user_name, Integer etat, Long id,Pageable pageable);

    ResponseEntity<ProduitDemande> ModifyDemenade(Long id, Integer etat, String message);

    ResponseEntity<?> getProduitDemandeById(Long id);

    ResponseEntity<?> getDemandeByUserAndProduit(Long iduser, Long idproduit);
}
