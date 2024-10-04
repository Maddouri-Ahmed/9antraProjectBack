package tn.esprit.PFE.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.PFE.entities.Email;


@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
}
