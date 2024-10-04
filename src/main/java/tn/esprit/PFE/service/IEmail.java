package tn.esprit.PFE.service;

import tn.esprit.PFE.entities.User;
import tn.esprit.PFE.entities.emailX;

public interface IEmail {
    emailX saveEmail(emailX e);
    emailX saveE(emailX e, User user);
}
