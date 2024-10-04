    package tn.esprit.PFE.repository;

    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.domain.Specification;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Repository;
    import tn.esprit.PFE.entities.Produits;
    import tn.esprit.PFE.entities.SexeType;
    import tn.esprit.PFE.entities.StatusUser;
    import tn.esprit.PFE.entities.User;
    import java.util.List;


    @Repository
    public interface UserRepository extends JpaRepository<User,Long> {
        User findByUsername(String username);
        List<User> findBySexeUser(SexeType sexeUser);
        List<User> findByAdressUser(String adressUser);
        User findByEmail(String email);
        Page<User> findAll(Specification<User> produitsSpecification, Pageable pageable);
        void deleteById(Long id);
        Page<User> findAll(Pageable pageable);

        Boolean existsByUsername(String username);
        Boolean existsByEmail(String email);


        @Query("SELECT COUNT(sexeUser) FROM User  WHERE sexeUser ='Women'")
        float getNumberWomen();
        @Query("SELECT COUNT(sexeUser) FROM User  WHERE sexeUser ='Men'")
        float getNumberMen();
        @Query("SELECT COUNT(id) FROM User ")
        float getNumberUser();
        List<User>findByStatusUser(StatusUser statusUser);





        @Query(value=" SELECT u from User  u where u.contri= TRUE ")
        List<User> find ();


        @Query(value=" SELECT u from User  u where u.idAdministrateur= :id ")
        List<User> findByUserAdministrateur (@Param("id") Long id);






        User findByConfirmationCode(String confirmationCode);



    }
