package tn.esprit.PFE.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import tn.esprit.PFE.entities.Notification;
import tn.esprit.PFE.entities.ProduitDemande;
import tn.esprit.PFE.exception.MapperException;

@Slf4j
@Component
public class KafkaConsumer {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(topics = "notification", groupId = "notification-group-id", containerFactory = "kakfaListenerContainerFactory")
    public void listenSenderEmail(String data) {

        Notification notification = fromJson(data, Notification.class);
        log.info("Consumed message: " + data);
        template.convertAndSend("/topic/notif", notification);
    }


    /**
     * Convert json to Object
     * @param json String json value
     * @param clazz Instances of the class
     * @param <T> Object Class
     * @return Object class
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new MapperException(e.getMessage());
        }
    }
}
