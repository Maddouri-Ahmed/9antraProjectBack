package tn.esprit.PFE.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tn.esprit.PFE.entities.Boit_tchat;

import java.util.List;

public interface BoitTchatRepository extends CrudRepository<Boit_tchat, Long>  {
		@Query(value="SELECT b FROM Boit_tchat b where b.e1=:id or b.e2=:id")
		 public List<Boit_tchat> BoitsUser (
				 @Param("id") Long id);
		 /*
		 @Query(value="select (SELECT b.e1 FROM Boit_tchat b where b in :Boites)")
		 public List<User> AmisUser (
				 @Param("Boites") List<Boit_tchat> Boites,
				 @Param("id") Long id);*/
		 @Query(value="SELECT b.IdBoit_tchat FROM Boit_tchat b where (b.e1=:id1 and b.e2=:id2) or (b.e1=:id2 and b.e2=:id1)")
		 public Long findByUsers (
				 @Param("id1") Long id1,
				 @Param("id2") Long id2);
		 
		 
}
