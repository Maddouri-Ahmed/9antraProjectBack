package tn.esprit.PFE.service;

import tn.esprit.PFE.entities.Message;

import java.util.List;

public interface Messageservice {
	 public Message addMessage(Message message);
	 	public void deleteMessage(Long id);
		public void addMessageToBoit(Message message, Long idBoite);
		public List<Message> AllMessageTwoAmis(Long idBoite);
		public Message LastMessage (Long idBoite) ;

}
