package tn.esprit.PFE.service.Impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import tn.esprit.PFE.dto.DemandeProduitDto;
import tn.esprit.PFE.entities.ProduitDemande;
import tn.esprit.PFE.entities.Produits;
import tn.esprit.PFE.entities.User;
import tn.esprit.PFE.exception.MapperException;
import tn.esprit.PFE.repository.IDemandeRepository;
import tn.esprit.PFE.repository.ProduitsRepository;
import tn.esprit.PFE.repository.UserRepository;
import tn.esprit.PFE.service.BrokerProducerService;
import tn.esprit.PFE.service.DemandeService;
import tn.esprit.PFE.utils.EmailServiceImpl;


import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j

public class DemandeProduitService implements DemandeService {

    private static final ObjectMapper mapper = new ObjectMapper();
    private BrokerProducerService brokerProducerService;
    private  Environment env;


    public  DemandeProduitService(BrokerProducerService brokerProducerService, Environment env) {
        this.brokerProducerService = brokerProducerService;
        this.env = env;


    }


    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    IDemandeRepository demanderepository ;
    @Autowired
    private ProduitsRepository produitrepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void sendD(ProduitDemande notificationDemande) {
        brokerProducerService.sendMessage(env.getProperty("producer.kafka.topic-name"), toJson(notificationDemande));
    }


    @Override
    public ResponseEntity<?> AddDemande(Long idproduit, Long user_id, String message){
        User user=userRepository.findById(user_id).orElse(null);
        if(user != null){
            ProduitDemande p=demanderepository.findByEtatAndUserIdAndProduitsId(0,user_id,idproduit);
            if(p==null){
                ProduitDemande produit = produitrepository.findById(idproduit).map(produits -> {
                    ProduitDemande demande = new ProduitDemande();
                    demande.setMessage(message);
                    demande.setProduits(produits);
                    demande.setUser(user);
                    demande.setEtat(0);
                    UUID uuid = UUID.randomUUID();
                    String uniqueId = uuid.toString().replaceAll("-", "").substring(0, 8);
                    demande.setCode(uniqueId);
                    String content = null;
                    // try {
                    //     content = new String(Files.readAllBytes(Paths.get("VerifyEmailTemplate.html")), StandardCharsets.UTF_8);
                    // } catch (IOException e) {
                    //     e.printStackTrace();
                    // }
                    // content = content.replace("{{link}}", "http://localhost:4200/home");
                    // // Send the email with HTML content
                    // emailService.sendSimpleEmail(user.getEmail(), "Welcome Mail", content);
                    // System.out.println("1");
                    return demanderepository.save(demande);
                }).orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + idproduit));
                return new ResponseEntity<>(produit, HttpStatus.CREATED);
            }else{
                demanderepository.delete(p);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }


    @Override
    public ProduitDemande save(ProduitDemande Demande) {
        emailService.sendSimpleEmail(Demande.getUser().getEmail(),
                "Welcome Mail",
                "Welcome To DevStore"+
                        "\n" +
                        "Bienvenue à DevStore,\n" +
                        "\n" +
                        "Merci d’avoir rejoint DevStore.\n" +
                        "\n" +
                        "Nous aimerions vous confirmer que votre demande de prduit "+Demande.getCode() +
                        "\n" +"a été créé avec succès.\n "+
                        "\n" +
                        "Si vous rencontrez des difficultés pour vous connecter à votre compte, contactez-nous à ahmedzarrai777@gmail.com.\n" +
                        "\n" +
                        "Cordialement,\n" +
                        "DevStore");
        emailService.sendSimpleEmail("testmailsenderspringboot",
                "Welcome Mail",
                "Welcome To DevStore"+
                        " demande de prduit "+Demande.getCode() +
                        "\n" +
                        "Cordialement,\n" +
                        "DevStore");
        return Demande;
    }

    @Override
    public ResponseEntity<Page<DemandeProduitDto>> getProduitDemande(String nomProduit, String user_name, Integer etat,Long id, Pageable pageable) {
        List<DemandeProduitDto> listdto = new ArrayList<>();

        Page<ProduitDemande> demandeProduitDtos = demanderepository.findAll((Specification<ProduitDemande>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Add filters based on the provided criteria
            if (etat != null ) {
                predicates.add(criteriaBuilder.equal(root.get("etat"), etat));
            }
            // Add filter for category name
            if (nomProduit != null && !nomProduit.isEmpty()) {
                Join<ProduitDemande, Produits> categoryJoin = root.join("produits");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("nom")), "%" + nomProduit.toLowerCase() + "%"));
            }

            if (user_name != null && !user_name.isEmpty()) {
                Join<ProduitDemande, User> categoryJoin = root.join("user");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("nom")), "%" + user_name.toLowerCase() + "%"));
            }

            if (id != null ) {
                Join<Produits, User> categoryJoin = root.join("user");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("id"), id));
            }

            query.orderBy(criteriaBuilder.asc(root.get("etat")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        for (ProduitDemande p : demandeProduitDtos.getContent()) {
            listdto.add(new DemandeProduitDto(p));
        }

        Page<DemandeProduitDto> produitsDtoPage = new PageImpl<>(listdto, pageable, demandeProduitDtos.getTotalElements());
        return new ResponseEntity<>(produitsDtoPage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProduitDemande> ModifyDemenade(Long id, Integer etat, String message){
        ProduitDemande demande=demanderepository.findById(id).orElse(null);
        if(demande!=null){
            demande.setEtat(etat);
            if(message!=null && !message.isEmpty()){
                demande.setAdminmessage(message);
            }
        }
        return new ResponseEntity<>(demande, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getProduitDemandeById(Long id) {
        DemandeProduitDto demandeProduitDto=new DemandeProduitDto(demanderepository.findById(id).get());
         return new ResponseEntity<>(demandeProduitDto, HttpStatus.OK);
    }

    /**
     * Convert Object to json
     *
     * @param object object
     * @return string json
     */
    private <T> String toJson(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new MapperException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getDemandeByUserAndProduit(Long iduser, Long idproduit){
        return new ResponseEntity<>(demanderepository.findByEtatAndUserIdAndProduitsId(0,iduser,idproduit), HttpStatus.OK);
    }

}
