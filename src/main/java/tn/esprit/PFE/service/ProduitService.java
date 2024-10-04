package tn.esprit.PFE.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.PFE.entities.*;
import tn.esprit.PFE.repository.ProduitRepository;
import tn.esprit.PFE.repository.UserRepository;
import tn.esprit.PFE.utils.EmailServiceImpl;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Service
@Transactional
@Slf4j
public class ProduitService implements IProduitservice{


    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    UserRepository urn ;
    @Autowired
    ProduitRepository ur ;
    @Autowired
    UserRepository pr ;
    //@Autowired
   // EmailService emailService;



    @Override
    public oldproduit saveProduit(oldproduit produit, Long id) {
        produit.setCategore(Categore.webproject);
        produit.setDate(LocalDate.now());
        produit.setTime(LocalTime.now());
        produit.setCode(RandomStringUtils.randomAlphanumeric(10));
        return ur.save(produit);
    }

    @Override
    public oldproduit saveProduitMobile(oldproduit produit, Long id) {
        produit.setCategore(Categore.mobilewebprojects);
        produit.setDate(LocalDate.now());
        produit.setTime(LocalTime.now());
        return ur.save(produit);
    }

    @Override
    public oldproduit saveProduitDesigne(oldproduit produit, Long id) {
        produit.setCategore(Categore.designgraphique);
        produit.setDate(LocalDate.now());
        produit.setTime(LocalTime.now());
        return ur.save(produit);
    }

    @Override
    public oldproduit saveProduitvideo(oldproduit produit, Long id) {
        produit.setTypev("videofile");
        produit.setCategore(Categore.videodesign);
        produit.setDate(LocalDate.now());
        produit.setTime(LocalTime.now());

        return ur.save(produit);
    }


    @Override
    public List<oldproduit> getProduits() {
        log.info("get oldproduit");
        return (List<oldproduit>) ur.findAll();
    }
    @Override
    public oldproduit findById(Long id) {
        return ur.findById(id).get();
    }

    public List<oldproduit> findByUser(Long  id) {
        return ur.findByUser(id);
    }

    public List<oldproduit> findByUserv(Long  id) {
        return ur.findByUserv(id);
    }


    @Override
    public List<oldproduit> retrieveProduitByCategore(Categore categore) {
        return ur.findByCategore(categore);
    }

    @Override
    public List<oldproduit> retrieveProduitByCatégories(catégories catégories) {
        return ur.findBycatégories(catégories);
    }

    @Override
    public List<oldproduit> retrieveProduitByOutil(Outil outil) {
        return ur.findByOutil(outil);
    }
    @Override
    public List<oldproduit> retrieveProduitByOutil1(Outil outil1) {
        return ur.findByOutil1(outil1);
    }
    @Override
    public List<oldproduit> retrieveProduitByOutil2(Outil outil2) {
        return ur.findByOutil2(outil2);
    }
    @Override
    public List<oldproduit> retrieveProduitByOutil3(Outil outil3) {
        return ur.findByOutil3(outil3);
    }
    @Override
    public List<oldproduit> retrieveProduitByOutil4(Outil outil4) {
        return ur.findByOutil4(outil4);
    }


    public void AcceptProduit(Long id,Long iduser ) {
        User user = pr.findById(iduser).get();

        oldproduit p = ur.findById(id).get();
        p.setPending(true);
        emailService.sendSimpleEmail(user.getEmail(),
                "Welcome Mail",
                "Welcome To DevStore"+
                        "\n" +
                        "Bienvenue à DevStore,\n" +
                        "\n" +
                        "Merci d’avoir rejoint DevStore.\n" +
                        "\n" +
                        "Nous aimerions vous confirmer que votre Produit Accepter.\n "+
                        "\n" +
                        "Cordialement,\n" +
                        "DevStore");
       // SimpleMailMessage mailMessage = new SimpleMailMessage();
       // mailMessage.setTo(user.getEmail());
       // mailMessage.setSubject("Confirm Registration!");
       // mailMessage.setFrom("ahmed.zarrai@esprit.tn");
       // mailMessage.setText("Produit Accepter");
        //emailService.sendEmail(mailMessage);
        ur.save(p);
        log.info("oldproduit-Verified");
    }


    @Override
    public oldproduit updateProductDesigne(oldproduit newProduct, Long id)
    {
        oldproduit oldProduct = ur.findById(id).get();

        if (!newProduct.getName().isEmpty())
            oldProduct.setName(newProduct.getName());

        if (!newProduct.getDescription().isEmpty())
            oldProduct.setDescription(newProduct.getDescription());

         //  if (!newProduct.getOutil3().toString().isEmpty())
         //   oldProduct.setOutil3(newProduct.getOutil3());

        if (newProduct.getPrix() != 0.0f)

            oldProduct.setPrix(newProduct.getPrix());


        //if ((!newProduct.getFileName().isEmpty()) )

           // oldProduct.setFileName(newProduct.getFileName());


        return ur.save(oldProduct);
    }


    @Override
    public oldproduit updateProductVideo(oldproduit newProduct, Long id)
    {
        oldproduit oldProduct = ur.findById(id).get();

        if (!newProduct.getName().isEmpty())
            oldProduct.setName(newProduct.getName());

        if (!newProduct.getDescription().isEmpty())
            oldProduct.setDescription(newProduct.getDescription());

        if (!newProduct.getOutil4().toString().isEmpty())
            oldProduct.setOutil4(newProduct.getOutil4());

        if (newProduct.getPrix() != 0.0f)
        {
            oldProduct.setPrix(newProduct.getPrix());
        }

        if ((!newProduct.getFileVideo().isEmpty()) )
        {
            oldProduct.setFileVideo(newProduct.getFileVideo());
        }

        return ur.save(oldProduct);
    }

    @Override
    public oldproduit updateProductMobile(oldproduit newProduct, Long id)
    {
        oldproduit oldProduct = ur.findById(id).get();

        if (!newProduct.getName().isEmpty())
            oldProduct.setName(newProduct.getName());

        if (!newProduct.getDescription().isEmpty())
            oldProduct.setDescription(newProduct.getDescription());

        if (newProduct.getPrix() != 0.0f)
        {
            oldProduct.setPrix(newProduct.getPrix());
        }

        if (!newProduct.getOutil2().toString().isEmpty())
            oldProduct.setOutil2(newProduct.getOutil2());


        if ((!newProduct.getFileName().isEmpty()) )
        {
            oldProduct.setFileName(newProduct.getFileName());
        }
        if ((!newProduct.getFileName1().isEmpty()) )
        {
            oldProduct.setFileName1(newProduct.getFileName1());
        }

        if ((!newProduct.getFileName2().isEmpty()) )
        {
            oldProduct.setFileName2(newProduct.getFileName2());
        }

        if ((!newProduct.getFileName3().isEmpty()) )
        {
            oldProduct.setFileName3(newProduct.getFileName3());
        }


        return ur.save(oldProduct);
    }

    @Override
    public oldproduit updateProductWeb(oldproduit newProduct, Long id)
    {
        oldproduit oldProduct = ur.findById(id).get();

        if (!newProduct.getName().isEmpty())
            oldProduct.setName(newProduct.getName());

        if (!newProduct.getDescription().isEmpty())
            oldProduct.setDescription(newProduct.getDescription());

        if (newProduct.getPrix() != 0.0f)
        {
            oldProduct.setPrix(newProduct.getPrix());
        }

        if (!newProduct.getOutil().toString().isEmpty())
            oldProduct.setOutil(newProduct.getOutil());

        if (!newProduct.getOutil1().toString().isEmpty())
            oldProduct.setOutil1(newProduct.getOutil1());


        if ((!newProduct.getFileName().isEmpty()) )
        {
            oldProduct.setFileName(newProduct.getFileName());
        }
        if ((!newProduct.getFileName1().isEmpty()) )
        {
            oldProduct.setFileName1(newProduct.getFileName1());
        }

        if ((!newProduct.getFileName2().isEmpty()) )
        {
            oldProduct.setFileName2(newProduct.getFileName2());
        }

        if ((!newProduct.getFileName3().isEmpty()) )
        {
            oldProduct.setFileName3(newProduct.getFileName3());
        }


        return ur.save(oldProduct);
    }



}
