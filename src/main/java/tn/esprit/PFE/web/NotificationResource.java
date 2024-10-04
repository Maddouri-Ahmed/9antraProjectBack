package tn.esprit.PFE.web;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.PFE.entities.Notification;
import tn.esprit.PFE.entities.ProduitDemande;
import tn.esprit.PFE.service.DemandeService;
import tn.esprit.PFE.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationResource {

    private final NotificationService notificationService;
    private  DemandeService demandeservice;

    public NotificationResource(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@RequestBody Notification notification) {
        notificationService.send(notification);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/sendD")
    public ResponseEntity<Void> sendNotificationDemande(@RequestBody ProduitDemande notificationDemande) {
        demandeservice.sendD(notificationDemande);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
