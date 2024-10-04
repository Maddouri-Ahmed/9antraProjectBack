package tn.esprit.PFE.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.PFE.entities.StatusUser;
import tn.esprit.PFE.entities.User;
import tn.esprit.PFE.repository.UserRepository;
import tn.esprit.PFE.utils.EmailServiceImpl;

import java.util.List;


@RestController
@RequestMapping("/api")
public class BanUserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailServiceImpl emailService;



    @GetMapping("/{email}/ban")
    public ResponseEntity<?> banUser(@PathVariable String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        user.setStatusUser(StatusUser.BANNED);
        userRepository.save(user);
        emailService.sendSimpleEmail(email, "Account Suspension Notification", "Dear User,\n" +
                "\n" +
                "We regret to inform you that your account has been suspended for violating our terms of service. This decision was made after a thorough evaluation of your activities on our platform.\n" +
                "\n" +
                "Please note that the suspension of your account is either temporary or permanent, depending on the severity of the violation. If you believe this action is unwarranted, you may contact us at noreply.pidev@gmail.com. \n Sincerely,\n" +
                "\n" +
                "Cocomarket Team");
        return ResponseEntity.ok("Account Banned Succefully");
    }

    @GetMapping("/{email}/unban")
    public ResponseEntity<?> unbanUser(@PathVariable String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        user.setStatusUser(StatusUser.ACTIVE);
        userRepository.save(user);
        return ResponseEntity.ok("Account Unbanned Succefully");
    }

    @GetMapping("/show-all-users")
    public List<User> getUtilisateursBannis() {
        return userRepository.findByStatusUser(StatusUser.BANNED);
    }

}
