package tn.esprit.PFE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.PFE.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}