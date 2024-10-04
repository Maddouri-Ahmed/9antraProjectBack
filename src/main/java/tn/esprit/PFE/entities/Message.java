package tn.esprit.PFE.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;



@Entity
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IdMessage")
	private Long IdMessage;
	private String text;
	private String Image;
    private Date date_denvoye;
	private Long employee_envoye;
	@ManyToOne
	@JsonIgnore
	Boit_tchat boit_tchat;
	public Long getIdMessage() {
		return IdMessage;
	}
	public void setIdMessage(Long idMessage) {
		IdMessage = idMessage;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public Date getDate_denvoye() {
		return date_denvoye;
	}
	public void setDate_denvoye(Date date_denvoye) {
		this.date_denvoye = date_denvoye;
	}
	public Long getEmployee_envoye() {
		return employee_envoye;
	}
	public void setEmployee_envoye(Long employee_envoye) {
		this.employee_envoye = employee_envoye;
	}
	public Boit_tchat getBoit_tchat() {
		return boit_tchat;
	}
	public void setBoit_tchat(Boit_tchat boit_tchat) {
		this.boit_tchat = boit_tchat;
	}
	public Message(Long idMessage, String text, String image, Date date_denvoye, Long employee_envoye,
			Boit_tchat boit_tchat) {
		super();
		IdMessage = idMessage;
		this.text = text;
		Image = image;
		this.date_denvoye = date_denvoye;
		this.employee_envoye = employee_envoye;
		this.boit_tchat = boit_tchat;
	}
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
