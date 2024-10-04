package tn.esprit.PFE.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.PFE.dto.NotificationDto;
import tn.esprit.PFE.entities.Notification;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class})
public interface NotificationMapper {

    @Mapping( source = "id",target = "id")
    @Mapping( source = "content",target = "content")

    @Mapping( source = "sender",target = "sender")
    @Mapping( source = "dateUpdated",target = "dateUpdated")
    @Mapping( source = "dateCreated",target = "dateCreated")
    NotificationDto notificationToNotificationDto(Notification notification);

    List<NotificationDto> notificationsToNotificationsDto(List<Notification> notifications);
}