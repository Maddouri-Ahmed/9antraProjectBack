package tn.esprit.PFE.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.PFE.entities.Commentaire;
import tn.esprit.PFE.service.CommentaireService;


import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api")
public class CommentaireCon {
    @Autowired
    CommentaireService cs ;

    @PostMapping("/commentair/{idpro}/{nameuser}")
    public void  saveCommentair(
            @PathVariable("nameuser") String nameuser,
            @PathVariable("idpro") Long idpro,
            @RequestParam(name = "description") String description)

    {
        Commentaire commentaire = new Commentaire();
        commentaire.setDescription(description);
        commentaire.setNameuser(nameuser);
        commentaire.setIdpro(idpro);
        cs.saveCommentaire(commentaire,nameuser ,idpro);
    }
    @GetMapping("/commentair/{id}")
    public List<Commentaire> getCommentair(@PathVariable("id") Long id) throws Exception {
        return cs.getCommentaire(id);
    }
}
