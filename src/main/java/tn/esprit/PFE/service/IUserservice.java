package tn.esprit.PFE.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.PFE.entities.Role;
import tn.esprit.PFE.entities.SexeType;
import tn.esprit.PFE.entities.User;
import tn.esprit.PFE.message.ResponseMessage;

import java.io.IOException;
import java.util.List;

public interface IUserservice {
    ResponseEntity<ResponseMessage> saveModérateur(User user);
    Role saveRole(Role role);
    void addRoleToUser(Long id , String name);
    User getUser(String username);
    List<User>getUsersX();
    List<User>getUsers();
    List<User> retrieveUserBySexe(SexeType sexeUser);
    List<User> retrieveUserByAdress(String adressUser);
    User findByEmail(String Email);
    User findById(Long id );
    List<User> findByUserAdministrateur(Long  id);
    List<User> retrieveUserByType(String type);
    User updatePhoto(User user, Long id);
    User updateUser (User user);
    public void updatePassword(Integer userId, String newPassword);


    ResponseEntity<Page<User>> GetUserBy(
            String nom,
            String role,
            Boolean compte,
            Boolean etat,
            Pageable pageable
    );

    void BanUser(Long id, boolean archive,String mesage) throws IOException;

    ResponseEntity<?> ModifyUserDetails(String us, MultipartFile image);

    void RefuseContri(Long id);

    ResponseEntity<?> EmailWithTemplate();

    ResponseEntity<?> EmailWithTemplateAndThread();

    ResponseEntity<?> EmailSendWithoutTemplate();
    String getUserRole(String username);

}
