package tn.esprit.PFE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.PFE.entities.Boit_tchat;
import tn.esprit.PFE.entities.Message;
import tn.esprit.PFE.repository.BoitTchatRepository;
import tn.esprit.PFE.repository.Messagerepo;
import tn.esprit.PFE.repository.UserRepository;

import java.util.*;
@Service
public class Messageserviceimpl implements Messageservice {
	private static final String List = null;
	private static final List<String> Bw = new ArrayList<>(Arrays.asList("fuck","bitch","ass","pussy","dick","boobs"));
	@Autowired
	Messagerepo messagerepo;
	@Autowired
	BoitTchatRepository boitTchatRepo;
	@Autowired
	UserRepository usrrepo;
	@Override
	public Message addMessage(Message message) {
		// TODO Auto-generated method stub
		String m= message.getText();
		String[] words= m.split("\\s");
		for(String e : Bw){ 
			for(int i=0 ; i< words.length;i++){
				if (words[i].toUpperCase().equals(e.toUpperCase())){
					String repeated = new String(new char[words[i].length()-2]).replace("\0", "*");
					words[i]=words[i].charAt(0)+repeated+words[i].charAt(words[i].length()-1);
				}	
			}
		}
		StringBuilder tmp = new StringBuilder();
		for(String e:words){
			tmp.append(e+" ");
		}
		String m2 = tmp.toString();
		
		message.setText(m2.substring(0, m2.length()-1));
		if (m != null && !m.equals("")) {
			messagerepo.save(message);
        }
		return message;
	}
	@Override
	public void addMessageToBoit(Message message,Long idBoite) {
		String m= message.getText();
		String[] words= m.split("\\s");
		for(String e : Bw){ 
			for(int i=0 ; i< words.length;i++){
				if (words[i].toUpperCase().equals(e.toUpperCase())){
					String repeated = new String(new char[words[i].length()-2]).replace("\0", "*");
					words[i]=words[i].charAt(0)+repeated+words[i].charAt(words[i].length()-1);
				}	
			}
		}
		StringBuilder tmp = new StringBuilder();
		for(String e:words){
			tmp.append(e+" ");
		}
		String m2 = tmp.toString();
		
		message.setText(m2.substring(0, m2.length()-1));
		
		
		Boit_tchat boite = boitTchatRepo.findById(idBoite).get();
		message.setBoit_tchat(boite);
		if (m != null && !m.equals("")) {
			messagerepo.save(message);
        }
	
		
		List<Message> messages = boite.getMessage(); 
		messages.add(message); 
		boite.setMessage(messages);
	}
	@Override
	public void deleteMessage(Long id) {
		// TODO Auto-generated method stub
		messagerepo.deleteById(id);
	}
	@Override
	
	public List<Message> AllMessageTwoAmis(Long idBoite) {
		// TODO Auto-generated method stub
		
		Boit_tchat boite = boitTchatRepo.findById(idBoite).get(); 
		List<Message> messages = boite.getMessage();
		Collections.sort(messages,new Comparator<Message>() {
			@Override
			public int compare(Message m1,Message m2) {
				return (int)(m1.getDate_denvoye().compareTo(m2.getDate_denvoye()));
			}
		});
		return messages;
	}
	

@Override
	public Message LastMessage (Long idBoite) {
		// TODO Auto-generated method stub
		List<Message> messagesSorted = AllMessageTwoAmis(idBoite);

	return messagesSorted.get(messagesSorted.size()-1);
	}

		
	



}
