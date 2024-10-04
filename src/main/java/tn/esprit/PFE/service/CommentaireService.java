package tn.esprit.PFE.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.PFE.entities.Commentaire;
import tn.esprit.PFE.repository.CommentaireR;


import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
@Transactional
@Slf4j
public class CommentaireService implements ICommentaireService {
    private static final String List = null;
    private static final List<String> Bw = new ArrayList<>(Arrays.asList("fuck","bitch","ass","pussy","dick","boobs"));

    @Autowired
    CommentaireR cr ;

    @Override
    public Commentaire saveCommentaire(Commentaire Commentaire, String nameuser, Long idpro) {
        String m= Commentaire.getDescription();
        String[] words= m.split("\\s");
        for(String e : Bw){
            for(int i=0 ; i< words.length;i++){
                if (words[i].toUpperCase().equals(e.toUpperCase())){
                    String repeated = new String(new char[words[i].length()-2]).replace("\0", "*");
                    words[i]=words[i].charAt(0)+repeated+words[i].charAt(words[i].length()-1);
                }
            }
        }
        Commentaire.setDate(LocalDate.now());
        Commentaire.setTime(LocalTime.now());
        return cr.save(Commentaire);
    }

    @Override
    public List<Commentaire> getCommentaire(Long id ) {
        return  cr.findByPro(id);
    }
}
