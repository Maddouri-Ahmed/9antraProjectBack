package tn.esprit.PFE.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tn.esprit.PFE.entities.Favoris;
import tn.esprit.PFE.entities.Produits;
import tn.esprit.PFE.entities.User;
import tn.esprit.PFE.repository.FavorisRepository;
import tn.esprit.PFE.repository.ProduitsRepository;
import tn.esprit.PFE.repository.Userrr;
import tn.esprit.PFE.service.IFavoris;

@Service
public class FavorisService implements IFavoris {
    @Autowired
    private FavorisRepository favorisRepository;
    @Autowired
    private Userrr userrr;
    @Autowired
    private ProduitsRepository produitsRepository;

    @Override
    public void AddFavoris(Long idu, Long idp){
        User user=userrr.findById(idu).orElse(null);
        Produits produits=produitsRepository.findById(idp).orElse(null);
        if(user!=null && produits!=null){
            Favoris favoris2=favorisRepository.findByUserIdAndProduitsId(user.getId(),produits.getId());
            if(favoris2!=null){
                favorisRepository.delete(favoris2);
            }else{
                Favoris favoris=new Favoris();
                favoris.setProduits(produits);
                favoris.setUser(user);
                favorisRepository.save(favoris);
            }
        }
    }


    @Override
    public ResponseEntity<?> GetFavoris(Long idu, Pageable pageable){
        Page<Favoris> favorises=favorisRepository.findByUserIdOrderByDateCreationDesc(idu,pageable);
        return new ResponseEntity<>(favorises, HttpStatus.OK);
    }

}
