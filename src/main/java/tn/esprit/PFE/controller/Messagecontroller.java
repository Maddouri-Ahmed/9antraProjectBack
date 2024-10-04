package tn.esprit.PFE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.PFE.entities.Message;
import tn.esprit.PFE.service.Messageservice;

import java.util.List;
@CrossOrigin(origins = {"*"})
@RequestMapping("/api")
@RestController
public class Messagecontroller {
	@Autowired
	Messageservice messageservice;

	/*
	@ResponseBody
	public  Message addMessage(@RequestBody Message message) {	
		messageservice.addMessage(message);

		return message;
	}
	*/

	@DeleteMapping("/admin/removeMessage/{IdMessage}")
	public void removeMessage(@PathVariable("IdMessage") Long IdMessage){
		messageservice.deleteMessage(IdMessage);
	}
	
	@PutMapping("/addMessageToBoit/{idBoite}")
	public void  addMessageToBoit(@RequestBody Message m, @PathVariable("idBoite") Long idBoite)
	{
	 	messageservice.addMessageToBoit(m, idBoite);	
  	}
	
	@GetMapping("/AllMessageTwoAmis/{idBoite}")
	public List<Message> AllMessageTwoAmis(@PathVariable("idBoite") Long idBoite)
	{
		return messageservice.AllMessageTwoAmis(idBoite);
	}
	@GetMapping("/LastMessage/{idBoite}")
	public Message LastMessage(@PathVariable("idBoite") Long idBoite)
	{
		return messageservice.LastMessage(idBoite);
	}
}
