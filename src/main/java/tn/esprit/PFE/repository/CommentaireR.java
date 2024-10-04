package tn.esprit.PFE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.PFE.entities.Commentaire;

import java.util.List;

@Repository
public interface CommentaireR extends JpaRepository<Commentaire,Long>{
    @Query(value=" SELECT u from Commentaire  u where u.idpro= :id ")
    List<Commentaire> findByPro (@Param("id") Long id);

}
