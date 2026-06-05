package com.example.devmatch.listener;

import com.example.devmatch.event.ApplicationCreatedEvent;
import com.example.devmatch.model.JobApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(ApplicationNotificationListener.class);

    @EventListener
    public void handleApplicationCreated(ApplicationCreatedEvent event) {
        JobApplication application = event.application();

        String jobTitle = application.getJobPosting().getTitle();
        String clientUsername = application.getJobPosting().getClient().getUsername();
        String clientEmail = application.getJobPosting().getClient().getEmail();
        String freelancerUsername = application.getFreelancer().getUsername();

        log.info("New application received for job '{}' from freelancer '{}'. Client '{}' would be notified at '{}'.",
                jobTitle,
                freelancerUsername,
                clientUsername,
                clientEmail
        );
    }
}