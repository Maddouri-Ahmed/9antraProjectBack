package tn.esprit.PFE.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IFavoris {
    void AddFavoris(Long idu, Long idp);

    ResponseEntity<?> GetFavoris(Long idu, Pageable pageable);
}
