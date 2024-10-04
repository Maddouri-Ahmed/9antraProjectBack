package tn.esprit.PFE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.PFE.entities.Newsletter;
import tn.esprit.PFE.repository.NewsletterRepository;

@Service
public class NewsletterService implements INewsletterService{
    @Autowired
    private NewsletterRepository newsletterRepository;

    @Override
    public Newsletter addNewsletter(Newsletter newsletter) {
        return newsletterRepository.save(newsletter);
    }

    @Override
    public void deleteNewsletter(Long id) {
        newsletterRepository.deleteById(id);
    }
}
