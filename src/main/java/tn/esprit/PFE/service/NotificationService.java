package tn.esprit.PFE.service;
    import tn.esprit.PFE.entities.Notification;
    import tn.esprit.PFE.entities.User;

    import java.util.List;

public interface NotificationService {
    /**
     * Send notification
     * @param notification model of notification
     */
    void send(Notification notification);


    Notification save(Notification notification , List<User> users , User sender) ;

    List<Notification> getNotifications();

    Notification getNotificationById(Long idNotification);

    Notification update(Notification notification, List<User> users, Long notificationId);

    void deleteNotification(Long id);
}
