package tn.esprit.PFE.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tn.esprit.PFE.entities.*;
import tn.esprit.PFE.payload.request.LoginRequest;
import tn.esprit.PFE.payload.response.JwtResponse;
import tn.esprit.PFE.payload.response.MessageResponse;
import tn.esprit.PFE.repository.ConfirmationTokenRepository;
import tn.esprit.PFE.repository.RoleRepository;
import tn.esprit.PFE.repository.UserRepository;
import tn.esprit.PFE.service.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.PFE.security.jwt.JwtUtils;
import tn.esprit.PFE.security.services.UserDetailsImpl;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import tn.esprit.PFE.service.Impl.UserService;
import tn.esprit.PFE.utils.EmailServiceImpl;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserController {


    @Autowired
    private ForgotPasswordService pwdservice;

    @Autowired
    UserService us ;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    //@Autowired
    //private EmailService emailService;
    @Autowired
    private RoleRepository rr;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private EmailServiceImpl emailService;

    String subPath = "products";
    @Autowired
    FileServiceImpl serviceFile;

    @Autowired
    private SpringTemplateEngine VerfiyEmailTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    ServletContext context;
    ObjectMapper objectMapper =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            User us = userRepository.findByEmail(loginRequest.getUsername());
            if (us != null) {
                loginRequest.setUsername(us.getUsername());
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userRepository.findByUsername(loginRequest.getUsername());

            if (user.getArchiver()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Votre compte est désactivé par l'administrateur"));
            }

            // Check if email is verified
            if (!user.isEmailValidated()) {
                return ResponseEntity.badRequest().body(new MessageResponse(" Merci de vérifier votre email avant de vous connecter."));
            }

            String jwt = jwtUtils.generateJwtToken(authentication);
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(
                    new JwtResponse(
                            jwt,
                            userDetails.getId(),
                            userDetails.getUsername(),
                            userDetails.getEmail(),
                            roles
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("There was a problem, please try again later: " + e.getMessage()));
        }
    }



    /*****************************************************save-CONTRIBUTEUR***************************************/
    @PostMapping(value="/registerContributeur")
    public ResponseEntity<?> RegisterCon(@RequestParam("user") String userr) throws IOException {
        User user = new Gson().fromJson(userr, User.class);
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Pseudo deja utilisé"));
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email deja utilisé"));
        }
        us.registerCONTRIBUTEUR(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    /*****************************************************save-CONTRIBUTEUR***************************************/

    @GetMapping(path="/ImagePro/{id}")
    public byte[] getPhoto(@PathVariable("id") Long id) throws Exception{
        User e = us.findById(id);
        return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+e.getFileName()));
    }


    /*****************************************************save-USER**************************************

   *****************************************************save-USER***************************************/
    @PostMapping("/registerUSER")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Pseudo deja utilisé"));
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email deja utilisé"));
        }
        return us.registerUSER(user);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> confirmEmail(@RequestParam("code") String confirmationCode) {
        User user = userRepository.findByConfirmationCode(confirmationCode);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid confirmation code");
        }

        if (user.getVerificationTokenExpiry().before(new Date())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Confirmation token has expired");
        }

        user.setEmailValidated(true);
        userRepository.save(user);
        return ResponseEntity.ok("Email verified successfully!");
    }



    /*****************************************************save-Moderateur**********************************************/
    @PostMapping("/save/Moderateur")
    @PreAuthorize("hasRole('ROLE_Administrateur')")
    public void SaveModerateur(@RequestBody User user) {
        us.saveModérateur(user);
    }
    /*****************************************************save-Moderateur*****************************************/

    /*****************************************************save-Administrateur*************************************/
    @PostMapping("/register-Administrateur")
    public void registerAdministrateur(@RequestBody User user) {
        us.registerAdministrateur(user);
    }
    /*****************************************************save-Administrateur*************************************/

    /*****************************************************save-USER**********************************************/


    /*****************************************************save-USER**********************************************/

    /*****************************************************get-all-user*******************************************/
    @GetMapping("/All/users")
    public ResponseEntity<List<User>>getUsers(){
        return ResponseEntity.ok().body(us.getUsers());
    }
    /*****************************************************get-all-user*******************************************/
    @GetMapping("/All/usersX")
    public ResponseEntity<List<User>>getUsersX(){
        return ResponseEntity.ok().body(us.getUsersX());
    }


    /*****************************************************new-role*******************************************/
    @PostMapping("/Role/save")

    public ResponseEntity<Role>SaveRole(@RequestBody Role role){
        URI uri =URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/role/save").toUriString());
        return ResponseEntity.created(uri).body(us.saveRole(role));
    }


    /*****************************************************new-role*******************************************/


    /*****************************************************add-role-to-user****************************************/
    @PostMapping("/changerUserRole")
    public ResponseEntity<?> addRoleToUser(@RequestParam("iduser") Long id,
                                           @RequestParam("role") String role){
        us.addRoleToUser(id,role);
        return ResponseEntity.ok().build();
    }

    /*****************************************************add-role-to-user****************************************/


    /*****************************************************confirm-account*******************************************/
    @GetMapping("/confirm-account")
    public void confirmUserAccountCompany( @RequestParam("token")String confirmationToken) {
        us.confirmUserAccountCONTRIBUTEUR(confirmationToken);
    }
    /*****************************************************confirm-account*******************************************/


    /*****************************************************active-account*******************************************/
    @GetMapping("/active-account")
    public void activeUserAccount( @RequestParam("token")String confirmationToken) {
    us.activeUserAccount(confirmationToken);
    }
    /*****************************************************active-account*******************************************/


    /******************************REST PASSWORD ******************************************************************/
    @GetMapping("/rssest-paword/{name}")
    public void demandToRestPassword(@PathVariable("name") String username){
       us.demandToRestPassword(username);
    }
    /******************************REST PASSWORD ******************************************************************/



    /******************************confirm-password******************************************************************/
    @GetMapping("/confirm-password/{new}/{confirm}")
    public void RestPassword( @RequestParam("token")String confirmationToken, @PathVariable("new") String NewPassword ,@PathVariable("confirm") String ConfirmPassword ){
        us.RestPassword(confirmationToken,NewPassword,ConfirmPassword);
    }
    /******************************confirm-password******************************************************************/



    /******************************retrieve-user-by-sexe******************************************************************/
    @GetMapping("/retrieve-user-by-sexe/{user-sexe}")
    @ResponseBody
    public List<User> retrieveUserBySexe(@PathVariable("user-sexe") tn.esprit.PFE.entities.SexeType sexeUser) {
        return us.retrieveUserBySexe(sexeUser);
    }

    /******************************retrieve-user-by-sexe******************************************************************/



    /******************************retrieve-user-by-adress******************************************************************/
    @GetMapping("/retrieve-user-by-adress/{user-adress}")
    @ResponseBody
    public List<User> retrieveUserByAdress(@PathVariable("user-adress") String adressUser) {
        return us.retrieveUserByAdress(adressUser);
    }
    /******************************retrieve-user-by-adress******************************************************************/

    /******************************retrieve-user-by-adress******************************************************************/
    @GetMapping("/retrieve-user-by-type/{user-role}")
    @ResponseBody
    public List<User> retrieveUserByRole(@PathVariable("user-role") String type) {
        return us.retrieveUserByType(type);
    }

    /******************************retrieve-user-by-adress******************************************************************/


    @GetMapping("/Contri/{name}")
    public void  retr(@PathVariable("name") String username) {us.demandeToBeContri(username);
    }
    @GetMapping("/Accepter/{name}")
    public void  Accepter (@PathVariable("name") String username) {us.demandeContriAccepter(username);
    }



    /******************************number-user******************************************************************/
    @GetMapping("/number-women")
    public float nomberWomen() throws Exception {
        return userRepository.getNumberWomen();
    }

    @GetMapping("/number-men")
    public float nomberMen() throws Exception {
        return userRepository.getNumberMen();
    }
    @GetMapping("/number-all")
    public float nomberU() throws Exception {
        return userRepository.getNumberUser();
    }

    @GetMapping("/number-CONTRIBUTEUR")
    public float nomberCONTRIBUTEUR() throws Exception {
        return rr.getNumberCONTRIBUTEUR();
    }

    @GetMapping("/number-user")
    public float nomberUser() throws Exception {
        return rr.getNumberUser();
    }

    @GetMapping("/number-contri")
    public List<User> nomberContri() throws Exception {
        return userRepository.find();
    }

    @GetMapping("/number-Modérateur")
    public float nomberModérateur() throws Exception {
        return rr.getNumberModérateur();
    }

    @GetMapping("/number-Admin")
    public float nomberAdmin() throws Exception {
        return rr.getNumberAdmin();
    }
    /******************************number-user******************************************************************/


    /******************************retrieve-user******************************************************************/
    @GetMapping("/retrieve-user-by-email/{user-email}")
    @ResponseBody
    public User retrieveUserByEmail(@PathVariable("user-email") String Email) {
        return us.findByEmail(Email);
    }

    @GetMapping("/retrieve-user-by-id/{user-id}")
    @ResponseBody
    public User retrieveUserById(@PathVariable("user-id") Long id) {
        return us.findById(id);
    }


   @GetMapping("/retrieve-user-by-Administrateur/{Administrateur-name}")
    @ResponseBody
    public List<User> findByUserCompany(@PathVariable("Administrateur-name") Long id ) {return us.findByUserAdministrateur(id);
    }










    //@GetMapping("/All/users/RUser")
    //public ResponseEntity<List<User>>retrieveuser(){
     //   return ResponseEntity.ok().body(us.getUsers());
   // }


    /******************************retrieve-user******************************************************************/

    @PostMapping(value="/register-USER", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<User> addEvent(@RequestPart("User") String pro, @RequestPart("file") MultipartFile file) throws IOException {
        String uploadDir = context.getRealPath("/Images/");

        // Ensure the directory exists
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
            System.out.println("Directory created: " + uploadDir);
        }

        // Create a unique filename to avoid overwriting
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String baseName = FilenameUtils.getBaseName(originalFilename);
        String newFileName = baseName + "_" + System.currentTimeMillis() + "." + extension;

        // Save the file to the specified directory
        File serverFile = new File(uploadDir + File.separator + newFileName);
        try {
            FileUtils.writeByteArrayToFile(serverFile, file.getBytes());
            System.out.println("Image saved: " + serverFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Deserialize the user object from the JSON string
        User newUser = objectMapper.readValue(pro, User.class);
        newUser.setFileName(newFileName);
        us.registerUSER(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }


    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long id,
                                           @RequestPart("User") String pro,
                                           @RequestPart("file") MultipartFile file,
                                           @RequestBody User userRequest) throws IOException {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (!existingUserOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User existingUser = existingUserOpt.get();

        String uploadDir = context.getRealPath("/Images/");

        // Ensure the directory exists
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
            System.out.println("Directory created: " + uploadDir);
        }

        // Create a unique filename to avoid overwriting
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String baseName = FilenameUtils.getBaseName(originalFilename);
        String newFileName = baseName + "_" + System.currentTimeMillis() + "." + extension;

        // Save the new file to the specified directory
        File serverFile = new File(uploadDir + File.separator + newFileName);
        try {
            FileUtils.writeByteArrayToFile(serverFile, file.getBytes());
            System.out.println("Image saved: " + serverFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Delete the old file if it exists
        if (existingUser.getFileName() != null) {
            File oldFile = new File(uploadDir + File.separator + existingUser.getFileName());
            if (oldFile.exists()) {
                oldFile.delete();
                System.out.println("Old image deleted: " + oldFile.getAbsolutePath());
            }
        }

        // Update the user details
        User updatedUser = objectMapper.readValue(pro, User.class);
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setFileName(newFileName);
        existingUser.setAdressUser(updatedUser.getAdressUser());
        existingUser.setCompetence(updatedUser.getCompetence());
        // Set other fields as needed

        userRepository.save(existingUser);
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }



    @PutMapping(value = "/updatePhotouser/{id}")
    public ResponseEntity<User> updateProductDesigne(@RequestParam(name = "fileName", required = false) MultipartFile fileName,
                                                     @PathVariable("id") Long id) throws IOException {
        Optional<User> userOpt = Optional.ofNullable(us.findById(id));
        if (!userOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userOpt.get();
        String imageName = user.getFileName();
        String uploadDir = context.getRealPath("/Images/");

        if (fileName != null) {
            // Ensure the directory exists
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
                System.out.println("Directory created: " + uploadDir);
            }

            // Create a unique filename to avoid overwriting
            String originalFilename = fileName.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            String baseName = FilenameUtils.getBaseName(originalFilename);
            String newFileName = baseName + "_" + System.currentTimeMillis() + "." + extension;

            // Save the new file to the specified directory
            File serverFile = new File(uploadDir + File.separator + newFileName);
            try {
                FileUtils.writeByteArrayToFile(serverFile, fileName.getBytes());
                System.out.println("Image saved: " + serverFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Delete the old file if it exists
            if (imageName != null) {
                File oldFile = new File(uploadDir + File.separator + imageName);
                if (oldFile.exists()) {
                    oldFile.delete();
                    System.out.println("Old image deleted: " + oldFile.getAbsolutePath());
                }
            }

            imageName = newFileName;
        }

        user.setFileName(imageName);
        us.updatePhoto(user, id);
        return ResponseEntity.ok(user);
    }

    @GetMapping(path="/ImageProsuser/{id}")
    public ResponseEntity<byte[]> getUserPhoto(@PathVariable("id") Long id) throws IOException {
        User user = us.findById(id);
        if (user == null || user.getFileName() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        File imgFile = new File(context.getRealPath("/Images/") + user.getFileName());
        if (!imgFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] image = Files.readAllBytes(imgFile.toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Adjust if you use different image types
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }



   /* @PutMapping("/usercontriall/{id}")
    public User updateUserContri(@PathVariable(value = "id") Long id, @RequestBody User userRequest) {
        return userRepository.findById(id).map(user -> {
              //  user.setUsername(userRequest.getUsername());
               // user.setEmail(userRequest.getEmail());
                user.setAdressUser(userRequest.getAdressUser());
                user.setPhoneNumberUser(userRequest.getPhoneNumberUser());
                user.setSexeUser(userRequest.getSexeUser());
                user.setCity(userRequest.getCity());
                user.setFacebook(userRequest.getFacebook());
                user.setInstagram(userRequest.getInstagram());
                user.setLinkedin(userRequest.getLinkedin());
                user.setGithub(userRequest.getGithub());
               // user.setCompetence(userRequest.getCompetence());
                user.setAbout(userRequest.getAbout());
                user.setPortfolio(userRequest.getPortfolio());
                return userRepository.save(user);

        }).orElseThrow(() -> new IllegalArgumentException("id "  + "Please active your compte"));
    }*/

    @PutMapping("/usercontriall/{id}")
    public User updateUserContri(@PathVariable(value = "id") Long id,
                                 @RequestPart(name = "file", required = false) MultipartFile file,
                                 @RequestPart("User") String pro) throws IOException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Handle image file if provided
            if (file != null) {
                String uploadDir = context.getRealPath("/Images/");

                // Ensure the directory exists
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                    System.out.println("Directory created: " + uploadDir);
                }

                // Create a unique filename to avoid overwriting
                String originalFilename = file.getOriginalFilename();
                String extension = FilenameUtils.getExtension(originalFilename);
                String baseName = FilenameUtils.getBaseName(originalFilename);
                String newFileName = baseName + "_" + System.currentTimeMillis() + "." + extension;

                // Save the file to the specified directory
                File serverFile = new File(uploadDir + File.separator + newFileName);
                try {
                    FileUtils.writeByteArrayToFile(serverFile, file.getBytes());
                    System.out.println("Image saved: " + serverFile.getAbsolutePath());

                    // Delete the old file if it exists
                    if (user.getFileName() != null) {
                        File oldFile = new File(uploadDir + File.separator + user.getFileName());
                        if (oldFile.exists()) {
                            oldFile.delete();
                            System.out.println("Old image deleted: " + oldFile.getAbsolutePath());
                        }
                    }

                    // Update user with new image filename
                    user.setFileName(newFileName);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Failed to save image: " + e.getMessage());
                }
            }

            // Update user details from JSON string
            User updatedUser = objectMapper.readValue(pro, User.class);
            user.setAdressUser(updatedUser.getAdressUser());
            user.setPhoneNumberUser(updatedUser.getPhoneNumberUser());
            user.setSexeUser(updatedUser.getSexeUser());
            user.setCity(updatedUser.getCity());
            user.setFacebook(updatedUser.getFacebook());
            user.setInstagram(updatedUser.getInstagram());
            user.setLinkedin(updatedUser.getLinkedin());
            user.setGithub(updatedUser.getGithub());
            user.setAbout(updatedUser.getAbout());
            user.setPortfolio(updatedUser.getPortfolio());

            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
    }


    @PutMapping("/usercontri/{id}")
    public User updateUserContriAll(@PathVariable(value = "id") Long id,
                                    @RequestPart(name = "file", required = false) MultipartFile file,
                                    @RequestPart("User") String pro) throws IOException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Handle image file if provided
            if (file != null) {
                String uploadDir = context.getRealPath("/Images/");

                // Ensure the directory exists
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                    System.out.println("Directory created: " + uploadDir);
                }

                // Create a unique filename to avoid overwriting
                String originalFilename = file.getOriginalFilename();
                String extension = FilenameUtils.getExtension(originalFilename);
                String baseName = FilenameUtils.getBaseName(originalFilename);
                String newFileName = baseName + "_" + System.currentTimeMillis() + "." + extension;

                // Save the file to the specified directory
                File serverFile = new File(uploadDir + File.separator + newFileName);
                try {
                    FileUtils.writeByteArrayToFile(serverFile, file.getBytes());
                    System.out.println("Image saved: " + serverFile.getAbsolutePath());

                    // Delete the old file if it exists
                    if (user.getFileName() != null) {
                        File oldFile = new File(uploadDir + File.separator + user.getFileName());
                        if (oldFile.exists()) {
                            oldFile.delete();
                            System.out.println("Old image deleted: " + oldFile.getAbsolutePath());
                        }
                    }

                    // Update user with new image filename
                    user.setFileName(newFileName);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Failed to save image: " + e.getMessage());
                }
            }

            // Update user details from JSON string
            User updatedUser = objectMapper.readValue(pro, User.class);
            user.setAdressUser(updatedUser.getAdressUser());
            user.setPhoneNumberUser(updatedUser.getPhoneNumberUser());
            user.setSexeUser(updatedUser.getSexeUser());
            user.setCity(updatedUser.getCity());
            user.setFacebook(updatedUser.getFacebook());
            user.setInstagram(updatedUser.getInstagram());
            user.setLinkedin(updatedUser.getLinkedin());
            user.setGithub(updatedUser.getGithub());
            user.setAbout(updatedUser.getAbout());
            user.setPortfolio(updatedUser.getPortfolio());

            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
    }




    /**********************************************************************************/





    /********************************----refresh Token-----***********************************************************************/
    @GetMapping("/token/refresh")
    public void refreshtoken(HttpServletRequest request , HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refrech_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refrech_token);
                String username = decodedJWT.getSubject();
                User user = us.getUser(username);
                String access_token = JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRoles().getName())
                        .sign(algorithm);
                Map<String ,String> tokens= new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refrech_token",refrech_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refressh token failed");
        }
    }
    /********************************----refresh Token-----****************************************************************/
    @GetMapping("/Userbyid/{IdUser}")
    public User Userbyid(@PathVariable("IdUser") Long IdUser ){
        return us.findById(IdUser);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            User user = userRepository.findByEmail(email);
            if(user!=null){
                String newPassword = pwdservice.generateNewPassword();
                pwdservice.sendPasswordResetEmail(email, newPassword);
                user.setPassword(passwordEncoder.encode(newPassword));
                us.updateUser(user);
                return ResponseEntity.ok("Password reset email sent");
            }
            return ResponseEntity.badRequest().body("User not found");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<?>  updatePassword(@RequestParam String email, @RequestParam String oldPassword, @RequestParam String newPassword) {
        User user = userRepository.findByEmail(email);
        boolean isMatch = passwordEncoder.matches(oldPassword, user.getPassword());
        if (isMatch) {
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedNewPassword);
            userRepository.save(user);
            return ResponseEntity.ok("Password updated successfully!");
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Please enter the right password"));


        }
    }

    @GetMapping("/GetUserBy")
    public ResponseEntity<?> GetUserBy(@RequestParam(value = "nom",required = false) String nom,
                                       @RequestParam(value = "role",required = false) String role,
                                       @RequestParam(value = "etat",required = false) Boolean etat,
                                       @RequestParam(value = "compte",required = false) Boolean compte,
                                        Pageable pageable){
        return us.GetUserBy(nom,role,compte,etat,pageable);
    }
    @GetMapping("/role/{username}")
    public String getUserRole(@PathVariable String username) {
        return us.getUserRole(username);
    }

    @PostMapping("/ChangeUserStatus")
    public void ChangeUserStatus(@RequestParam("id") Long id,
                                 @RequestParam("archive") boolean archive,
                                 @RequestParam(value = "message",required = false) String message) throws IOException {
         us.BanUser(id,archive,message);
    }

    @PostMapping("/ModifyUserDetails")
    public ResponseEntity<?> ModifyUserDetails(@RequestParam("user") String user,
                                               @RequestParam(value = "image",required = false) MultipartFile image){
        System.out.println(user);
        return us.ModifyUserDetails(user,image);
    }

    @PostMapping("/RefuseContri")
        public void RefuseContri(@RequestParam("iduser") Long id){
            us.RefuseContri(id);
        }

@PostMapping("/EmailWithTemplate")
    public ResponseEntity<?> EmailWithTemplate(){
        return us.EmailWithTemplate();
}
    @PostMapping("/EmailWithTemplateAndThread")
    public ResponseEntity<?> EmailWithTemplateAndThread(){
        return us.EmailWithTemplateAndThread();
    }
    @PostMapping("/EmailSendWithoutTemplate")
    public ResponseEntity<?> EmailSendWithoutTemplate(){
        return us.EmailSendWithoutTemplate();
    }
}



@Data
class RoleToUserForm{
    private String username;
    private String name;
}
