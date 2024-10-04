package tn.esprit.PFE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.PFE.Mapper.NotificationMapper;
import tn.esprit.PFE.Response.NotificationResponse;
import tn.esprit.PFE.dto.NotificationDto;
import tn.esprit.PFE.entities.Notification;
import tn.esprit.PFE.entities.User;
import tn.esprit.PFE.repository.NotificationRepository;
import tn.esprit.PFE.repository.UserRepository;
import tn.esprit.PFE.service.NotificationService;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class NotificationController {


    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private NotificationRepository notificationRepository;


    @PostMapping("/send/{userId}")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationResponse notificationResponse , @PathVariable Long userId) {
        List<User> users = new ArrayList<>();
        for (String email : notificationResponse.getEmailList()) {
            User receiver = userRepository.findByEmail(email);
            users.add(receiver);
        }
        User sender = userRepository.findById(userId).get();

        Notification notification =  notificationService.save( notificationResponse.getNotification() , users , sender) ;
        System.out.println("notifications" + notification);
        notificationService.send(notification);
        return new ResponseEntity<>( notification , HttpStatus.OK);
    }


    @GetMapping("/notifications")
    public ResponseEntity<?> GetNotifications() {
        List<Notification> list = notificationService.getNotifications();
        List<NotificationDto> listdto = notificationMapper.notificationsToNotificationsDto(list);
        return new ResponseEntity<>(listdto, HttpStatus.OK);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<?> GetNotificationById( @PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        return new ResponseEntity<>(notification, HttpStatus.OK);

    }


    @DeleteMapping("/deletenoti/{id}")
    public ResponseEntity<?> dele( @PathVariable Long id) {
        notificationRepository.deleteById(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }


    @PutMapping("/update/{notificationId}")
    public ResponseEntity<Notification> updateNotification(@PathVariable Long notificationId, @RequestBody NotificationResponse notificationResponse) {
        List<User> users = new ArrayList<>();
        for (String email : notificationResponse.getEmailList()) {
            User receiver = userRepository.findByEmail(email);
            users.add(receiver);
        }
        Notification updatedNotification = notificationService.update(notificationResponse.getNotification(), users, notificationId);
        notificationService.send(updatedNotification);
        return new ResponseEntity<>( updatedNotification ,HttpStatus.OK);
    }}
