package tn.esprit.PFE.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.PFE.entities.Favoris;

public interface FavorisRepository extends JpaRepository<Favoris, Long> {
    Favoris findByUserIdAndProduitsId(Long idu,Long idp);
    Page<Favoris> findByUserIdOrderByDateCreationDesc(Long idu, Pageable pageable);
}
