package tn.esprit.PFE.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.PFE.repository.IDemandeRepository;
import tn.esprit.PFE.repository.ProduitsRepository;
import tn.esprit.PFE.repository.UserRepository;
import tn.esprit.PFE.service.Impl.DemandeProduitService;
import tn.esprit.PFE.service.NotificationService;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api")
public class DemandeProduitController {


    @Autowired
    DemandeProduitService demandeproduitservice ;
    @Autowired
    private IDemandeRepository iDemandeRepository;
    @Autowired
    private ProduitsRepository produitrepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/addDemandeproduit")
    public ResponseEntity<?> createComment(@RequestParam(value = "produitId") Long produitId,
                                           @RequestParam(name = "user_id") Long user_id,
                                           @RequestParam(name = "message") String message) {
        return demandeproduitservice.AddDemande(produitId,user_id,message);
    }


    @GetMapping("/AllDemandeProduit")
    public ResponseEntity<?>getDemande(@RequestParam(value = "nom_produit",required = false) String nom_produit,
                                       @RequestParam(value = "nom_user",required = false) String nom_user,
                                       @RequestParam(value = "etat",required = false) Integer etat,
                                       @RequestParam(value = "iduser",required = false) Long id,
                                       Pageable pageable){
        return demandeproduitservice.getProduitDemande(nom_produit,nom_user,etat,id,pageable);
    }

    @PostMapping("modifyDemande")
    public ResponseEntity<?> modifyDemande(@RequestParam("id") Long id,
                                           @RequestParam("etat") Integer etat,
                                           @RequestParam("message") String meassage){
        return demandeproduitservice.ModifyDemenade(id,etat,meassage);
    }

    @GetMapping("/RetrieveDemande/{id}")
    @ResponseBody
    public ResponseEntity<?> RetrieveDemandeById(@PathVariable("id") Long id) {
        return demandeproduitservice.getProduitDemandeById(id);
    }
    @GetMapping("/numberD")
    public float nomberDemande() throws Exception {
        return iDemandeRepository.getNumberDemande();
    }

    @GetMapping("/numberDEnAttente")
    public float nomberDemandeEnAttente() throws Exception {
        return iDemandeRepository.getNumberDemandeEnAttente();
    }

    @GetMapping("/numberDRefuser")
    public float nomberDemandeRefuser() throws Exception {
        return iDemandeRepository.getNumberDemandeRefuser();
    }

    @GetMapping("/numberDAccepter")
    public float nomberDemandeAccepter() throws Exception {
        return iDemandeRepository.getNumberDemandeAccepter();
    }

    @GetMapping("/chiffreAffaireTotalProduitVendu")
    public float chiffreAffaireTotalProduitVendu() throws Exception {
        return iDemandeRepository.getChiffreAffaireTotalProduitVendu();
    }

    // getNumberDemandeEnAttente
    @GetMapping("getNumberDemandeEnAttente")
    public float getNumberDemandeEnAttente(){
        return iDemandeRepository.getNumberDemandeEnAttente();
    }
    // getNumberDemandeRefuser
    @GetMapping("getNumberDemandeRefuser")
    public float getNumberDemandeRefuser(){
        return iDemandeRepository.getNumberDemandeRefuser();
    }
    // getNumberDemandeAccepter
    @GetMapping("getNumberDemandeAccepter")
    public float getNumberDemandeAccepter(){
        return iDemandeRepository.getNumberDemandeAccepter();
    }

    @GetMapping("/getDemandeByUserAndProduit")
    public ResponseEntity<?> getDemandeByUserAndProduit(@RequestParam("iduser") Long idu,
                                                        @RequestParam("idproduit")Long idp){
        return demandeproduitservice.getDemandeByUserAndProduit(idu,idp);
    }



}
