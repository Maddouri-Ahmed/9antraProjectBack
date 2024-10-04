package tn.esprit.PFE.service;

import tn.esprit.PFE.entities.Boit_tchat;
import tn.esprit.PFE.entities.User;

import java.util.List;

public interface BoitTchatService {

	public void ajouterBoit(Long IdUser1,Long IdUser2);

	public List<Boit_tchat> AllBoitUser(Long IdUser);

	public List<User> AllAmis(Long IdUser);

	public Long BoitIdUsers(Long IdUser1, Long IdUser2);


}
