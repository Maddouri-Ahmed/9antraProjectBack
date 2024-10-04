package tn.esprit.PFE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.PFE.entities.ERole;
import tn.esprit.PFE.entities.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);


    @Query("SELECT COUNT(name) FROM Role  WHERE name ='ROLE_CONTRIBUTEUR'")
    float getNumberCONTRIBUTEUR();
    @Query("SELECT COUNT(name) FROM Role  WHERE name ='ROLE_USER'")
    float getNumberUser();
    @Query("SELECT COUNT(name) FROM Role  WHERE name ='ROLE_Modérateur'")
    float getNumberModérateur();
    @Query("SELECT COUNT(name) FROM Role  WHERE name ='ROLE_Administrateur'")
    float getNumberAdmin();
    Optional<Role> findByName(ERole name);
}
