package tn.esprit.PFE.Mapper;

import org.mapstruct.Mapper;
import tn.esprit.PFE.dto.UserNotificationDto;
import tn.esprit.PFE.entities.UserNotification;
import java.util.List;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring", uses = { UserMapper.class})


public interface UserNotificationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "receiver.email", target = "receiverEmail")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "notification.sender.email", target = "senderEmail")
    @Mapping(source = "notification.sender.id", target = "senderId")
    @Mapping(source = "notification", target = "notification")
    @Mapping(source = "isSeen", target = "isSeen")

    UserNotificationDto userNotificationToUserNotificationDto(UserNotification userNotification) ;
    List<UserNotificationDto> userNotificationsToUserNotificationsDtos(List<UserNotification> notifications);
}