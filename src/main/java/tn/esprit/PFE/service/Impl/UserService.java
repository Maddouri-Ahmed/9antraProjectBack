package tn.esprit.PFE.service.Impl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.controller.UserNotFoundException;
import tn.esprit.PFE.entities.*;
import tn.esprit.PFE.message.ResponseMessage;
import tn.esprit.PFE.payload.response.MessageResponse;
import tn.esprit.PFE.repository.ConfirmationTokenRepository;
import tn.esprit.PFE.repository.RoleRepository;
import tn.esprit.PFE.repository.UserRepository;
import tn.esprit.PFE.utils.EmailServiceImpl;
import tn.esprit.PFE.service.IUserservice;
import tn.esprit.PFE.utils.SaveFiles;

import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
@Transactional
@Slf4j
public class UserService implements IUserservice, UserDetailsService {


    private final long EXPIRATION_TIME = 86400000;
    @Autowired
    UserRepository ur ;
    @Autowired
    RoleRepository rr;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
     ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    EmailServiceImpl emailService;
    @Autowired
    CategoriesService categoriesService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private SaveFiles saveFiles;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       User user = ur.findByUsername(username);
       if(user==null){
           log.info("user noy found");
           throw new UsernameNotFoundException("user not found");
       }else {
           log.info("username :{}",username);
       }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
           authorities.add(new SimpleGrantedAuthority(user.getRoles().getName()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), authorities);
    }



    //------------------- CONTRIBUTEUR wil create account -------------------


    //-------------------   USER wil create account -------------------

    /************************************----- CONTRIBUTEUR --------*************************/


    //------------------- CONTRIBUTEUR wil create account -------------------

    public ResponseEntity<ResponseMessage> registerCONTRIBUTEUR(User user) {
        User existingUser = ur.findByUsername(user.getUsername());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Pseudo deja utilisé"));
        }

        Role ROLE_CONTRIBUTEUR = rr.findByName(ERole.ROLE_CONTRIBUTEUR.name());
        if (ROLE_CONTRIBUTEUR == null) {
            ROLE_CONTRIBUTEUR = new Role(ERole.ROLE_CONTRIBUTEUR.name());
            ROLE_CONTRIBUTEUR = rr.save(ROLE_CONTRIBUTEUR);
        }

        user.setRoles(ROLE_CONTRIBUTEUR);
        user.setContri(true);
        user.setArchiver(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (ur.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Email deja utilisé"));
        }

        // Generate a confirmation code and set it for the user
        String confirmationCode = UUID.randomUUID().toString();
        user.setConfirmationCode(confirmationCode);
        user.setVerificationTokenExpiry(new Date(System.currentTimeMillis() + EXPIRATION_TIME));  // Set token expiry date

        user.setFileName(null);
        ur.save(user);

        // Send the verification email
        String subject = "Please verify your email address";
        String body = "Dear " + user.getUsername() + ",<br><br>"
                + "Thank you for registering as a contributor. Please click the link below to verify your email address:<br>"
                + "<a href=\"http://localhost:8089/api/verify?code=" + confirmationCode + "\">Verify your email</a><br><br>"
                + "Thank you,<br>Your Company";

        // Invoke the email sending method
        emailService.sendSimpleEmails(user.getEmail(), subject, body);

        return ResponseEntity.ok(new ResponseMessage("Contributor registered successfully! Please check your email to verify your address."));
    }

    public ResponseEntity<ResponseMessage> registerUSER(User user) {
        User existingUser = ur.findByUsername(user.getUsername());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Pseudo deja utilisé"));
        }

        Role ROLE_USER = rr.findByName(ERole.ROLE_USER.name());
        if (ROLE_USER == null) {
            ROLE_USER = new Role(ERole.ROLE_USER.name());
            ROLE_USER = rr.save(ROLE_USER);
        }

        user.setArchiver(false);
        user.setRoles(ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (ur.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Email deja utilisé"));
        }

        // Generate a confirmation code and set it for the user
        String confirmationCode = UUID.randomUUID().toString();
        user.setConfirmationCode(confirmationCode);
        user.setVerificationTokenExpiry(new Date(System.currentTimeMillis() + EXPIRATION_TIME));  // Set token expiry date
        user.setFileName(null);

        ur.save(user);

        // Send the verification email
        String subject = "Please verify your registration";
        String body = "Dear " + user.getUsername() + ",<br><br>"
                + "Thank you for registering. Please click the link below to verify your email address:<br>"
                + "<a href=\"http://localhost:8089/api/verify?code=" + confirmationCode + "\">Verify your email</a><br><br>"
                + "Thank you,<br>9antra Project";

        // Invoke the email sending method
        emailService.sendSimpleEmails(user.getEmail(), subject, body);

        return ResponseEntity.ok(new ResponseMessage("User registered successfully!"));
    }





    public String confirmEmail(String confirmationCode) {
        User user = ur.findByConfirmationCode(confirmationCode);

        if (user == null) {
            return "Invalid confirmation code";
        }

        if (user.getVerificationTokenExpiry().before(new Date())) {
            return "Confirmation token has expired";
        }

        user.setEmailValidated(true);
        ur.save(user);
        return "Email verified successfully!";
    }
    //------------------- USER wil create account -------------------



        //-------------------   USER wil create account -------------------



        //------------------- USER wil create account -------------------

        //-------------------   USER wil create account -------------------

        public ResponseEntity<ResponseMessage> registerAdministrateur(User user) {
            User existingUser = ur.findByUsername(user.getUsername());
            if(existingUser != null)
            {log.info("This username already exists!");}
            else
            {
                Role ROLE_USER = rr.findByName(ERole.ROLE_Administrateur.name());
                if(ROLE_USER==null){
                    ROLE_USER = new Role(ERole.ROLE_Administrateur.name());
                    ROLE_USER=rr.save(ROLE_USER);
                }
                user.setRoles(ROLE_USER);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                if (ur.findByEmail(user.getEmail()) != null) {
                    return ResponseEntity.badRequest().body(new ResponseMessage("Error: Email is already taken!"));
                }

                ur.save(user);


            }
            return null;
        }
        //------------------- USER wil create account -------------------





        //------------------- active company account -------------------
        public void confirmUserAccountCONTRIBUTEUR(String confirmationToken)
        {
            ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
            if(token != null)
            {
                User user = ur.findById(token.getUserEntity().getId()).orElse(null);
                user.setIsEnable(true);
                ur.save(user);

            }
        }
        //------------------- active company account -------------------





        //-------- saveModérateur ---------------//
        @Override
    public ResponseEntity<ResponseMessage> saveModérateur(User user) {
        User existingUser = ur.findByUsername(user.getUsername());
        User user1 = new User();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails)principal).getUsername();
            user1= ur.findByUsername(username);
        } else {
            String username = principal.toString();
            user1= ur.findByUsername(username);
        }
        if(existingUser != null)
        {
            log.info("This email already exists!");
        }else {
            Role ROLE_USER = rr.findByName(ERole.ROLE_Modérateur.name());
            if(ROLE_USER==null){
                ROLE_USER = new Role(ERole.ROLE_Modérateur.name());
                ROLE_USER=rr.save(ROLE_USER);
            }
            user.setRoles(ROLE_USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEmail(user.getEmail());
            user.setRoles(ROLE_USER);
            user.setIdAdministrateur(user1.getId());

            if  (ur.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.badRequest().body(new ResponseMessage("Error: Email is already taken!"));
            }
            ur.save(user);
            log.info("saving new user");
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            confirmationTokenRepository.save(confirmationToken);
          //  SimpleMailMessage mailMessage = new SimpleMailMessage();
          ///  mailMessage.setTo(user.getEmail());
         //   mailMessage.setSubject("Activé compte");
          //  mailMessage.setFrom("ahmed.zarrai@esprit.tn");
          //  mailMessage.setText("To confirm your account, please click here : "
          //          +"http://localhost:8089/api/active-account?token="+confirmationToken.getConfirmationToken()+"---------------------------" +
           //         ""+"username "+":"+user.getUsername()+"----------"+"password "+":"+user.getPassword());
          //  emailService.sendEmail(mailMessage);

           // SimpleMailMessage mailMessage1 = new SimpleMailMessage();
          ///  mailMessage1.setTo(user1.getEmail());
          //  mailMessage1.setSubject("Invitation Status");
           // mailMessage1.setFrom("ahmed.zarrai@esprit.tn");
           // mailMessage1.setText("your invitation has been successfully send to  : "+user.getEmail());
           // emailService.sendEmail(mailMessage1);
        }
        return null;
    }



    //-------- saveModérateur ---------------//




    //---------company will  add new role ----------------
    @Override
    public Role saveRole(Role role) {
        log.info("saving new role");
        return rr.save(role);
    }
    //---------company will  add new role ----------------



    // --------campany will add role to user ------------
    @Override
    public void addRoleToUser(Long id, String name) {
        log.info("add role to usser");
        User user = ur.findById(id).orElse(null);
        if(user!=null){
            Role role = rr.findByName(name);
            if(role==null){
                role=new Role(ERole.ROLE_CONTRIBUTEUR.toString());
                role=rr.save(role);
            }
            user.setRoles(role);
            ur.save(user);
        }
    }



    // --------campany will add role to user ------------

    // ------------campany get user by username --------------------
    @Override
    public User getUser(String username) {
        log.info("get user");
        return ur.findByUsername(username);
    }

    @Override
    public List<User> getUsersX() {
       User user = new User();
       user.getUsername();
       user.getRoles();
        return (List<User>) user;
    }
    // ------------campany get user by username --------------------


    // ------------user get all user by username sexeUser adressUser  Email----------------
    @Override
    public List<User> getUsers() {
        log.info("get users");
        return ur.findAll();
    }

    @Override
    public List<User> retrieveUserBySexe(SexeType sexeUser) {
        return ur.findBySexeUser(sexeUser);
    }

    @Override
    public List<User> retrieveUserByAdress(String adressUser) {
        return ur.findByAdressUser(adressUser);
    }




    @Override
    public User findByEmail(String email) {
        return ur.findByEmail(email);
    }

    @Override
    public User findById(Long id)  {
        return ur.findById(id).get();
    }


    @Override
    public List<User> findByUserAdministrateur(Long  id) {
        return ur.findByUserAdministrateur(id);
    }

    @Override
    public List<User> retrieveUserByType(String type) {
        return null;
    }


    // ------------user get all user by username sexeUser adressUser  Email----------------


    /**********************------user area-------*****************/

    /********* ------------ app will active account user -------------*/

    public void activeUserAccount(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null)
       {
            User user = ur.findByUsername(token.getUserEntity().getUsername());
            user.setIsEnable(true);
            ur.save(user);
            log.info("accountVerified");
       }
       else {log.info("The link is invalid or broken!");}
    }

    public void demandeToBeContri(String username) {
            User user = ur.findByUsername(username);
            user.setContri(true);
            ur.save(user);
            log.info("accountVerified");

    }

    /********* ------------ app will active account user -------------*/



    public void demandeContriAccepter(String username) {
        User user = ur.findByUsername(username);
        user.setContri(false);
        ur.save(user);
        log.info("accountVerified");
    }

    /****************************** REST PASSWORD ********************************/

    /*****----------------------Demande To Rest PWD-----------------------*******/
    public void demandToRestPassword(@PathVariable("name") String username){
        User user = ur.findByUsername(username);
        if(user !=null){
            ConfirmationToken restPwdToken = new ConfirmationToken(user);
            confirmationTokenRepository.save(restPwdToken);

            //Random rand = new Random();

         //   SimpleMailMessage mailMessage = new SimpleMailMessage();
          //  mailMessage.setTo(user.getEmail());
          //  mailMessage.setSubject("rest password ! ");
          // // mailMessage.setFrom("ahmed.zarrai@esprit.tn");
           // mailMessage.setText("To rest your password take code : " + restPwdToken.getConfirmationToken() );
           // emailService.sendEmail(mailMessage);
           // log.info("/rest-password sended");
        } else {log.info("does not exist");}
    }
    /*****----------------------Demande To Rest PWD-----------------------*******/


    /*******----------------------REST PWD --------------------------------*********/
    public void RestPassword(String confirmationToken, String NewPassword ,String ConfirmPassword ){
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token!=null){
            User user = token.getUserEntity();

            user.setPassword(passwordEncoder.encode(NewPassword));
            ur.save(user);
            log.info("done!");
        }
        else {log.info("The link is invalid or broken!");}
    }

    /*******----------------------REST PWD --------------------------------*********/

    /****************************** REST PASSWORD ********************************/



    /*************************** Scheduler **********************************************/
                       /*
                       @Scheduled(fixedRate = 60000)
                       public  void reminderEmployeesToActiveAccount(){
                       List<User> users = ur.findAll();
                       List<User> usersdisable = new ArrayList<>();
                       usersdisable = users.stream().filter(e->!e.isEnable()).collect(Collectors.toList());
                       for (User u: usersdisable) {
                       log.info("{}",u.getUsername());
                       ConfirmationToken restPwdToken = new ConfirmationToken(u);
                       confirmationTokenRepository.save(restPwdToken);
                       SimpleMailMessage mailMessage = new SimpleMailMessage();
                       mailMessage.setTo(u.getEmail());
                       mailMessage.setSubject("reminder to confirm account ! ");
                       mailMessage.setFrom("ahmed.zarrai@esprit.tn");
                       mailMessage.setText("To confirm your account, please click here : "
                       +"http://localhost:8089/api/active-account?token="+ restPwdToken.getConfirmationToken());
                       emailService.sendEmail(mailMessage);
                       log.info("/rest-password sended");
                       }
                       }*/
    /*************************** Scheduler *********************************************/


    @Override
    public User updatePhoto(User user, Long id)
    {
        User oldUser = ur.findById(id).get();
        if ((!user.getFileName().isEmpty()) )
        {
            oldUser.setFileName(user.getFileName());
        }
        return ur.save(oldUser);
    }

    @Override
    public User updateUser(User user) {
        System.out.print("Information(s) successfully updated");
        return ur.save(user);
    }

    @Override
    public void updatePassword(Integer userId, String newPassword) {
       Optional<User> userOptional = ur.findById(Long.valueOf(userId));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(newPassword);
            ur.save(user);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

@Override
    public ResponseEntity<Page<User>> GetUserBy(
        String nom,
        String role,
        Boolean compte,
        Boolean etat,
        Pageable pageable
){

    Page<User> UsersPage = ur.findAll((Specification<User>) (root, query, criteriaBuilder) -> {
        List<Predicate> predicates = new ArrayList<>();

        // Add filters based on the provided criteria
        if (nom != null && !nom.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + nom.toLowerCase() + "%"));
        }


        if (role != null && !role.isEmpty()) {
            Join<User, Role> categoryJoin = root.join("roles");
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("name")), "%" + role.toLowerCase() + "%"));
        }

        if (etat != null ) {
            predicates.add(criteriaBuilder.equal(root.get("contri"), etat));
        }



        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }, pageable);

    return new ResponseEntity<>(UsersPage, HttpStatus.OK);

}
@Override
public void BanUser(Long id, boolean archive,String message) throws IOException {
        User user=ur.findById(id).orElse(null);
        user.setArchiver(archive);
        ur.save(user);
        if(message!=null && !message.isEmpty()){
            String content = new String(Files.readAllBytes(Paths.get("Ban.html")), StandardCharsets.UTF_8);
            content = content.replace("{{body}}", message );
            emailService.sendSimpleEmail(user.getEmail(), "Account Status", content);
        }else if((message ==null || message.isEmpty()) && archive){
            String content = new String(Files.readAllBytes(Paths.get("Ban.html")), StandardCharsets.UTF_8);
            emailService.sendSimpleEmail(user.getEmail(), "Account Status", content);
        }
}

    @Override
    public ResponseEntity<?> ModifyUserDetails(String us, MultipartFile image) {
        User user = new Gson().fromJson(us, User.class);
        User user2 = ur.findById(user.getId()).orElse(null);

        if (user2 != null) {
            // Check if the provided password matches the stored password
            if (passwordEncoder.matches(user.getPassword(), user2.getPassword())) {
                // Check if the email is being updated and already exists
                if (!user2.getEmail().equals(user.getEmail()) && ur.findByEmail(user.getEmail()) != null) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Email already exists!"));
                }
                // Check if the username is being updated and already exists
                if (!user2.getUsername().equals(user.getUsername()) && ur.findByUsername(user.getUsername()) != null) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Username already exists!"));
                }

                // Update user details
                user2.setContri(user.isContri());
                user2.setUsername(user.getUsername() != null ? user.getUsername() : user2.getUsername());
                user2.setAbout(user.getAbout() != null ? user.getAbout() : user2.getAbout());
                user2.setEmail(user.getEmail() != null ? user.getEmail() : user2.getEmail());
                user2.setFullname(user.getFullname() != null ? user.getFullname() : user2.getFullname());
                user2.setPhoneNumberUser(user.getPhoneNumberUser() >= 0 ? user.getPhoneNumberUser() : user2.getPhoneNumberUser());
                user2.setCompetence(user.getCompetence() != null ? user.getCompetence() : user2.getCompetence());
                user2.setGithub(user.getGithub() != null ? user.getGithub() : user2.getGithub());
                user2.setLinkedin(user.getLinkedin() != null ? user.getLinkedin() : user2.getLinkedin());
                user2.setAdressUser(user.getAdressUser() != null ? user.getAdressUser() : user2.getAdressUser());
                user2.setFacebook(user.getFacebook() != null ? user.getFacebook() : user2.getFacebook());
                user2.setInstagram(user.getInstagram() != null ? user.getInstagram() : user2.getInstagram());
                user2.setBibliographie(user.getBibliographie() != null ? user.getBibliographie() : user2.getBibliographie());
                // Handle image update if provided
                if (image != null) {
                    // Delete existing file if any
                    if (user2.getFileName() != null) {
                        saveFiles.DeleteFile(user2.getFileName());
                    }
                    // Save new image file
                    ResponseEntity<?> response = saveFiles.SaveFile(image, "Users/" + String.valueOf(user2.getId()) + "_" + user2.getUsername());
                    Object responseBody = response.getBody();
                    if (responseBody instanceof Map) {
                        Map<String, String> responseMap = (Map<String, String>) responseBody;
                        String path = responseMap.get("path");
                        user2.setFileName(path);
                    }
                }

                // Save updated user object
                user2 = ur.save(user2);
                return new ResponseEntity<>(user2, HttpStatus.OK);
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("Incorrect password"));
            }
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Account not found"));
    }


@Override
    public void RefuseContri(Long id){
        User user=ur.findById(id).orElse(null);
        if(user!=null){
            user.setContri(false);
        }
}

@Override
    public ResponseEntity<?> EmailWithTemplate(){
    try {
        String body = new String(Files.readAllBytes(Paths.get("VerifyEmailTemplate.html")), StandardCharsets.UTF_8);
        body = body.replace("{{link}}", "http://localhost:4200/home/" );
        // Créer une instance de Properties pour configurer la session JavaMail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        // Créer une instance de javax.mail.Session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("testmailsenderspringboot@gmail.com", "fiwmwfqeffdepoju");
            }
        });
        javax.mail.Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("testmailsenderspringboot@gmail.com"));
        message.setContent(body, "text/html");
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("nejdbedoui@gmail.com"));
        message.setSubject("EmailWithTemplate");
        Transport.send(message);
    } catch (MessagingException e) {
        emailService.sendSimpleEmail2("nejdbedoui@gmail.com","EmailWithTemplate error","EmailWithTemplate error1 #"+e+"# message: "+e.getMessage());
        return ResponseEntity.badRequest().body(new MessageResponse("error1 "+e.getMessage()));
    } catch (IOException e) {
        e.printStackTrace();
        emailService.sendSimpleEmail2("nejdbedoui@gmail.com","EmailWithTemplate error","EmailWithTemplate error2 "+e+" message: "+e.getMessage());
        return ResponseEntity.badRequest().body(new MessageResponse("EmailWithTemplate error sending mail error2 "+e+" message: "+e.getMessage()));
    }
    return new ResponseEntity<>("EmailWithTemplate done", HttpStatus.OK);
}


    @Override
    public ResponseEntity<?> EmailWithTemplateAndThread(){
        try {
            new Thread(() -> {
                try {
                    String body = new String(Files.readAllBytes(Paths.get("VerifyEmailTemplate.html")), StandardCharsets.UTF_8);
                    body = body.replace("{{link}}", "http://localhost:4200/home/" );
                    // Créer une instance de Properties pour configurer la session JavaMail
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", "587");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    Session session = Session.getInstance(props, new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("testmailsenderspringboot@gmail.com", "fiwmwfqeffdepoju");
                        }
                    });
                    javax.mail.Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("testmailsenderspringboot@gmail.com"));
                    message.setContent(body, "text/html");
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("nejdbedoui@gmail.com"));
                    message.setSubject("EmailWithTemplateAndThread");
                    Transport.send(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    emailService.sendSimpleEmail2("nejdbedoui@gmail.com","EmailWithTemplateAndThread error","EmailWithTemplateAndThread error in thread "+e+" message: "+e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            emailService.sendSimpleEmail2("nejdbedoui@gmail.com","EmailWithTemplateAndThread error","EmailWithTemplateAndThread error "+e+" message: "+e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("EmailWithTemplateAndThread error" + e.getMessage()));
        }
        return new ResponseEntity<>("EmailWithTemplateAndThread done", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> EmailSendWithoutTemplate(){
        try {
            emailService.sendSimpleEmail2("nejdbedoui@gmail.com","EmailSendWithoutTemplate no error","EmailSendWithoutTemplate no error");
        }catch (Exception e){
            emailService.sendSimpleEmail2("nejdbedoui@gmail.com","EmailSendWithoutTemplate error","EmailSendWithoutTemplate error "+e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("EmailSendWithoutTemplate error" + e+" message: "+e.getMessage()));
        }
        return new ResponseEntity<>("EmailSendWithoutTemplate done", HttpStatus.OK);
    }

    @Override
    public String getUserRole(String username) {
        User user = ur.findByUsername(username);
        if (user != null && user.getRoles() != null) {
            // Assuming roles_id directly maps to Role's id field
            Long roleId = user.getRoles().getId();
            // Logic to map roles_id to actual role name
            String roleName = mapRoleIdToRoleName(roleId); // Implement this method
            return roleName;
        }
        return null;
    }

    // Method to map roles_id to role name based on your logic
    private String mapRoleIdToRoleName(Long roleId) {
        // Example logic (replace with your actual mapping logic)
        switch (roleId.intValue()) {
            case 1:
                return "ROLE_USER";
            case 2:
                return "ROLE_ADMIN";
            case 3:
                return "ROLE_CONTRIBUTEUR";
            default:
                return "ROLE_UNKNOWN";
        }
    }
}
