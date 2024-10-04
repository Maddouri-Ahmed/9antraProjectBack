package tn.esprit.PFE.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.PFE.entities.emailX;

@Repository
public interface IEmailR extends CrudRepository<emailX, Long> {
}
