package tn.esprit.PFE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.PFE.entities.Newsletter;

public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
}