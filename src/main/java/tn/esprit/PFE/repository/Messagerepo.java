package tn.esprit.PFE.repository;

import org.springframework.data.repository.CrudRepository;
import tn.esprit.PFE.entities.Message;

public interface Messagerepo extends CrudRepository<Message, Long>  {

}
