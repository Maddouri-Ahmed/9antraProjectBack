package tn.esprit.PFE.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.PFE.entities.User;
import tn.esprit.PFE.entities.emailX;
import tn.esprit.PFE.service.Email;
import tn.esprit.PFE.repository.UserRepository;
import tn.esprit.PFE.utils.EmailServiceImpl;
import java.net.URI;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api")
public class emailC {
    @Autowired
    Email es;

    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailServiceImpl emailService;

    @PostMapping("/email/save")
    public ResponseEntity<emailX> SaveRole(@RequestBody emailX e){
        URI uri =URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/role/save").toUriString());
        return ResponseEntity.created(uri).body(es.saveEmail(e));

    }
    @PostMapping("/email/save/C")
    public ResponseEntity<emailX> SaveRole(@RequestBody emailX e, User user){
        URI uri =URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/role/save").toUriString());
        return ResponseEntity.created(uri).body(es.saveE(e,user));

    }

    @PostMapping("/email/sendToAdmin")
    public ResponseEntity<String> sendEmailToAdmin(@RequestBody Map<String, String> requestBody) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username);
        if (user != null) {


            String name = requestBody.get("name");
            String email = requestBody.get("email");
            String phoneNumber = requestBody.get("phoneNumber");
            String subject = requestBody.get("subject");
            String messageContent = requestBody.get("message");

            emailService.sendUserDetailsEmail(name, email,phoneNumber, subject, messageContent);
            return ResponseEntity.ok("Email sent successfully!");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

}
