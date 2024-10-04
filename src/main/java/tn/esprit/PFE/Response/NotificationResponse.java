package tn.esprit.PFE.Response;

import lombok.*;
import tn.esprit.PFE.entities.Notification;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse{

    private List<String> emailList;
    private Notification notification;
}