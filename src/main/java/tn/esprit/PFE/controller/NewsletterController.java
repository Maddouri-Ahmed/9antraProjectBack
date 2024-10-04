package tn.esprit.PFE.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.PFE.entities.Newsletter;
import tn.esprit.PFE.service.NewsletterService;

@RestController
@RequestMapping("/newsletters")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

    @PostMapping("/add")
    public ResponseEntity<Newsletter> addNewsletter(@RequestBody Newsletter newsletter) {
        Newsletter createdNewsletter = newsletterService.addNewsletter(newsletter);
        return ResponseEntity.ok(createdNewsletter);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNewsletter(@PathVariable Long id) {
        newsletterService.deleteNewsletter(id);
        return ResponseEntity.noContent().build();
    }
}