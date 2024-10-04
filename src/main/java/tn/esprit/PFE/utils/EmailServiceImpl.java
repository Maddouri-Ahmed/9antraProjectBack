package tn.esprit.PFE.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tn.esprit.PFE.repository.UserRepository;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Properties;

@Service
public class EmailServiceImpl {
    @Value("${app.email}")
    private String email;
    @Value("${app.mdp}")
    private String mdp;

    @Autowired
    UserRepository userRepository;

    @Async
    public void sendSimpleEmails(String toEmail, String subject, String body) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, mdp);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(body, "text/html");

            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            // Consider logging the exception here or rethrowing it
            e.printStackTrace();  // This will print the stack trace to help with debugging
        }
    }
    @Async
    public void sendSimpleEmail(String toEmail,
                                String subject,
                                String body
    ) {

       // new Thread(() -> {
            // Email sending logic goes here
        try {
            System.out.println("2");
            // Créer une instance de Properties pour configurer la session JavaMail
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            // Créer une instance de javax.mail.Session
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, mdp);
                }
            });
            // Créer un objet MimeMessage pour représenter l'e-mail
            Message message = new MimeMessage(session);
            // Configurer l'e-mail en définissant le destinataire, l'expéditeur, le sujet et le corps
            message.setFrom(new InternetAddress(email));
            message.setContent(body, "text/html");
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            // Envoyer l'e-mail
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'e-mail: " + e.getMessage());
        }
        //}).start();

    }


    public void sendSimpleEmail2(String toEmail,
                                String subject,
                                String body
    ) {
        // Créer une instance de Properties pour configurer la session JavaMail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Créer une instance de javax.mail.Session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, mdp);
            }
        });

        try {
            // Créer un objet MimeMessage pour représenter l'e-mail
            Message message = new MimeMessage(session);

            // Configurer l'e-mail en définissant le destinataire, l'expéditeur, le sujet et le corps
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Envoyer l'e-mail
            Transport.send(message);

            System.out.println("E-mail envoyé avec succès!");

        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'e-mail: " + e.getMessage());
        }
    }


        public void receiveMail() {
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imaps");
        properties.setProperty("mail.imap.ssl.enable", "true");

        try {
            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore();
            store.connect("imap.gmail.com", email, mdp);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            for (Message message : messages) {

                System.out.println("From: " + Arrays.toString(message.getFrom()));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("Sent Date: " + message.getSentDate());
                System.out.println("Content: " + message.getContent().toString());
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendUserDetailsEmail(String name, String email,String phoneNumber, String subject, String messageContent) {
        String toEmail = "amaddouri03@gmail.com";
        String subjectLine = "Message from " + name;
        String body = "Name: " + name + "<br>" +
                "Email: " + email + "<br>" +
                "Phone Number: " + phoneNumber + "<br>" +
                "Subject: " + subject + "<br>" +
                "Message: " + messageContent;

        sendSimpleEmail(toEmail, subjectLine, body);
    }

}