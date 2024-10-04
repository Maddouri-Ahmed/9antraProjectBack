package tn.esprit.PFE.dto;
import lombok.*;
import tn.esprit.PFE.entities.NotificationType;
import java.util.Date;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {


    private Long id;
    private String title;
    private String content;
    private NotificationType type;
    private UserDto sender;
    //  private List<UserNotificationDto> receivers;
    private Date dateCreated;
    private Date dateUpdated;
}