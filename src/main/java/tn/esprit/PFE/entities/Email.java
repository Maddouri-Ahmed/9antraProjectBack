package tn.esprit.PFE.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "from_email")
    private String from;
    @Column(name = "to_email")
    private String to;
    @Column(name = "subject")
    private String subject;
    @Column(name = "body")
    private String body;
    @Column(name = "sent_date")
    private LocalDateTime sentDate;
    @Column(name = "received_date")
    private LocalDateTime receivedDate;
}
