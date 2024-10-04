package tn.esprit.PFE.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import tn.esprit.PFE.entities.Notification;
import tn.esprit.PFE.entities.User;
import tn.esprit.PFE.entities.UserNotification;
import tn.esprit.PFE.exception.MapperException;
import tn.esprit.PFE.repository.NotificationRepository;
import tn.esprit.PFE.repository.UserNotificationRepository;
import tn.esprit.PFE.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;


@Service
public class NotificationServiceImpl  implements NotificationService {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final BrokerProducerService brokerProducerService;
    private final Environment env;


    private final UserRepository userRepository ;
    private final NotificationRepository notificationRepository ;
    private final UserNotificationRepository userNotificationRepository ;


    public NotificationServiceImpl(BrokerProducerService brokerProducerService, Environment env
            , NotificationRepository notificationRepository , UserRepository userRepository, UserNotificationRepository userNotificationRepository) {
        this.brokerProducerService = brokerProducerService;
        this.env = env;
        this.notificationRepository=notificationRepository;
        this.userRepository =userRepository;
        this.userNotificationRepository =userNotificationRepository;
    }

    @Override
    public void send(Notification notification) {
        brokerProducerService.sendMessage(env.getProperty("producer.kafka.topic-name"), toJson(notification));
    }

    @Override
    public Notification save(Notification notification  , List<User> users , User sender) {
        notification.setSender(sender);
        notification.setDateCreated(new Date());
        Notification newNotification = notificationRepository.save(notification);
        for (User user : users) {
            UserNotification userNotification = new UserNotification();
            userNotification.setId(null);
            userNotification.setReceiver(user);
            userNotification.setNotification(newNotification);
            userNotification.setIsSeen(false);
            userNotificationRepository.save(userNotification);
        }

        return newNotification;
    }

    @Override
    public List<Notification> getNotifications() {
        return (List<Notification>) notificationRepository.findAll();
    }
    @Override
    public Notification getNotificationById(Long idNotification) {
        return notificationRepository.findById(idNotification).get();
    }

    @Override
    public Notification update(Notification notification, List<User> users, Long notificationId) {
        Notification originalNotification = notificationRepository.findById(notificationId).orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        originalNotification.setTitle(notification.getTitle());
        originalNotification.setContent(notification.getContent());
        originalNotification.setDateUpdated(new Date());
        Notification updatedNotification = notificationRepository.save(originalNotification);

        // Delete all user notifications for the original notification
        List <UserNotification> list =  userNotificationRepository.findByNotificationId(notificationId);

        for( UserNotification userNotification : list){
            userNotificationRepository.delete(userNotification);
        }

        // Create new user notifications for all receivers of the updated notification
        for (User user : users) {
            UserNotification userNotification = new UserNotification();
            userNotification.setId(null);
            userNotification.setReceiver(user);
            userNotification.setNotification(updatedNotification);

            userNotification.setIsSeen(false);
            userNotificationRepository.save(userNotification);
        }

        return updatedNotification;
    }


    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }



    /**
     * Convert Object to json
     *
     * @param object object
     * @return string json
     */
    private <T> String toJson(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new MapperException(e.getMessage());
        }
    }
}
