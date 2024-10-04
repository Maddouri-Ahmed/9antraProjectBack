package tn.esprit.PFE.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tn.esprit.PFE.entities.User;
import tn.esprit.PFE.entities.emailX;
import tn.esprit.PFE.repository.IEmailR;
import tn.esprit.PFE.repository.UserRepository;

import javax.transaction.Transactional;

@Service

@Transactional
@Slf4j
public class Email implements IEmail {

    @Autowired
    IEmailR er;

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository ur;

    @Override
    public emailX saveEmail(emailX e) {
        SimpleMailMessage mailMessage1 = new SimpleMailMessage();
        mailMessage1.setTo("ahmed.zarrai@esprit.tn");
        mailMessage1.setSubject("Invitation Status");
        mailMessage1.setFrom(e.getEmail());
        mailMessage1.setText("your invitation has been successfully send to  : " + e.getMessage());
        emailService.sendEmail(mailMessage1);
        return er.save(e);

    }

    @Override
    public emailX saveE(emailX e, User user) {


        User existingUser = ur.findByUsername(user.getUsername());
        User user1 = new User();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user1 = ur.findByUsername(username);
        } else {
            String username = principal.toString();
            user1 = ur.findByUsername(username);
        }
        if (existingUser != null) {
            log.info("This email already exists!");
        } else {
            e.setIdCompany(user1.getId());
            er.save(e);
            log.info("saving new news");
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user1.getEmail());
            mailMessage.setSubject("publication");
            mailMessage.setFrom("ahmed.zarrai@esprit.tn");
            mailMessage.setText("your mail has been successfully add :" +e.getMessage());
            emailService.sendEmail(mailMessage);
        }
        return e;
    }
}
