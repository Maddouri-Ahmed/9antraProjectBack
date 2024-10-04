package tn.esprit.PFE.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationDto {
    private Long id;
    private String  receiverEmail;
    private Long  receiverId;
    private Long  senderId ;
    private String  senderEmail;
    private NotificationDto notification;
    private Boolean isSeen;

}