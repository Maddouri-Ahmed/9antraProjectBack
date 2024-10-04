package tn.esprit.PFE.service;

import tn.esprit.PFE.entities.Commentaire;

import java.util.List;


public interface ICommentaireService {
    Commentaire saveCommentaire(Commentaire Commentaire, String  nameuser,Long idpro);
    List<Commentaire> getCommentaire(Long id);

}
