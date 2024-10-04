package tn.esprit.PFE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.PFE.Mapper.UserNotificationMapper;
import tn.esprit.PFE.dto.UserNotificationDto;
import tn.esprit.PFE.entities.UserNotification;
import tn.esprit.PFE.service.UserNotificationService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class UserNotificationController {

    @Autowired
    UserNotificationService userNotificationService;
    @Autowired
    UserNotificationMapper userNotificationMapper ;


    @GetMapping("/notifications/User/{userId}/{notificationId}")
    public ResponseEntity<?> GetNotificationsReceiversByUser(@PathVariable Long userId , @PathVariable Long notificationId) {
        List<UserNotification> list = userNotificationService.getNotificatinReceiversBynotificationAndByUser(notificationId ,userId);
        List<UserNotificationDto> userNotificationDtos =  userNotificationMapper.userNotificationsToUserNotificationsDtos(list) ;
        return new ResponseEntity<>(userNotificationDtos, HttpStatus.OK);
    }


    @GetMapping("/notifications/mark-seen/{userId}")
    public ResponseEntity<?> markAllSeen(@PathVariable Long userId) {
        userNotificationService.markAllSeen(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/notifications/{userId}")
    public ResponseEntity<?> GetNotificationsByReceiver( @PathVariable Long userId) {
        List<UserNotification> list = userNotificationService.getNotificationsForAuthUser(userId) ;
        List<UserNotificationDto> listDtos =  userNotificationMapper.userNotificationsToUserNotificationsDtos(list);
        return new ResponseEntity<>(listDtos, HttpStatus.OK);
    }




    @GetMapping("/notifications/read/{id}")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long id) {
        userNotificationService.markNotificationAsRead(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }


    @GetMapping("/notifications/get/{id}")
    public ResponseEntity<?> getUserNotificationById(@PathVariable Long id) {
        UserNotification userNotification = userNotificationService.getUserNotificationById(id);
        UserNotificationDto userNotificationDto=  userNotificationMapper.userNotificationToUserNotificationDto(userNotification) ;
        return new ResponseEntity<>( userNotificationDto, HttpStatus.OK);
    }


    @DeleteMapping("/deleteNotiUser/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        userNotificationService.deleteUserNotification(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}