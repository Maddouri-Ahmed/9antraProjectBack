package tn.esprit.PFE.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "userNotification")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @JsonIgnore
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    @JsonIgnore
    private Notification notification;

    private Boolean isSeen;


}
