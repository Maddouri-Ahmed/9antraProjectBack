package tn.esprit.PFE.service;

import tn.esprit.PFE.entities.Newsletter;

public interface INewsletterService {
    Newsletter addNewsletter(Newsletter newsletter);
    void deleteNewsletter(Long id);
}
