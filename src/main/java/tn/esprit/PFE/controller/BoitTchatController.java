package tn.esprit.PFE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.PFE.entities.Boit_tchat;
import tn.esprit.PFE.entities.User;
import tn.esprit.PFE.service.BoitTchatService;

import java.util.List;
@CrossOrigin(origins = {"*"})
@RequestMapping("/api")

@RestController
public class BoitTchatController {
	@Autowired
	BoitTchatService BoitTchatServ ;

	@GetMapping("/AllBoitUser/{IdEmployee}")
	public List<Boit_tchat> AllBoitUser(@PathVariable("IdEmployee") Long IdUser){
		return BoitTchatServ.AllBoitUser(IdUser);
	}


	@PutMapping("/ajouterBoit/{IdEmployee1}/{IdEmployee2}")
	public void  ajouterBoit(@PathVariable("IdEmployee1") Long IdUser1,@PathVariable("IdEmployee2") Long IdUser2) {
		
		BoitTchatServ.ajouterBoit(IdUser1, IdUser2);	
  	}

	

	@GetMapping("/BoitIdUsers/{IdUser1}_{IdUser2}")
	public Long BoitIdUsers(@PathVariable("IdUser1") Long IdUser1,@PathVariable("IdUser2")Long IdUser2) {
		return BoitTchatServ.BoitIdUsers(IdUser1,IdUser2);
	}
	@GetMapping("/AllAmis/{IdEmployee}")
	public List<User> AllAmis(@PathVariable("IdEmployee") Long IdUser){
		return BoitTchatServ.AllAmis(IdUser);
	}
	
}
