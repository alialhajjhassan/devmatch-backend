package com.example.devmatch.service;

import com.example.devmatch.dto.ApplicationResponse;
import com.example.devmatch.dto.CreateApplicationRequest;
import com.example.devmatch.dto.UpdateApplicationStatusRequest;
import com.example.devmatch.exception.DuplicateApplicationException;
import com.example.devmatch.exception.InvalidApplicationStatusException;
import com.example.devmatch.exception.ResourceNotFoundException;
import com.example.devmatch.exception.UnauthorizedActionException;
import com.example.devmatch.model.ApplicationStatus;
import com.example.devmatch.model.JobApplication;
import com.example.devmatch.model.JobPosting;
import com.example.devmatch.model.Role;
import com.example.devmatch.model.User;
import com.example.devmatch.repository.JobApplicationRepository;
import com.example.devmatch.repository.JobPostingRepository;
import org.springframework.stereotype.Service;
import com.example.devmatch.event.ApplicationCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CurrentUserService currentUserService;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            JobPostingRepository jobPostingRepository,
            ApplicationEventPublisher eventPublisher,
            CurrentUserService currentUserService
    ) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.eventPublisher = eventPublisher;
        this.currentUserService = currentUserService;
    }

    public ApplicationResponse applyToJob(Long jobId, CreateApplicationRequest request) {
        User authenticatedUser = currentUserService.getAuthenticatedUser();

        if (authenticatedUser.getRole() != Role.FREELANCER) {
            throw new UnauthorizedActionException("Only FREELANCER users can apply to job postings");
        }

        JobPosting jobPosting = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found with id: " + jobId));

        boolean alreadyApplied = jobApplicationRepository.existsByJobPostingAndFreelancer(
                jobPosting,
                authenticatedUser
        );

        if (alreadyApplied) {
            throw new DuplicateApplicationException("You have already applied to this job posting");
        }

        JobApplication application = new JobApplication();
        application.setJobPosting(jobPosting);
        application.setFreelancer(authenticatedUser);
        application.setCoverLetter(request.coverLetter());

        JobApplication savedApplication = jobApplicationRepository.save(application);

        eventPublisher.publishEvent(new ApplicationCreatedEvent(savedApplication));

        return mapToApplicationResponse(savedApplication);
    }

    private ApplicationResponse mapToApplicationResponse(JobApplication application) {
        return new ApplicationResponse(
                application.getId(),
                application.getJobPosting().getId(),
                application.getJobPosting().getTitle(),
                application.getFreelancer().getId(),
                application.getFreelancer().getUsername(),
                application.getCoverLetter(),
                application.getStatus(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }


    public ApplicationResponse updateApplicationStatus(
            Long applicationId,
            UpdateApplicationStatusRequest request
    ) {
        User authenticatedUser = currentUserService.getAuthenticatedUser();

        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + applicationId
                ));

        JobPosting jobPosting = application.getJobPosting();

        if (authenticatedUser.getRole() != Role.CLIENT) {
            throw new UnauthorizedActionException("Only CLIENT users can update application status");
        }

        if (!jobPosting.getClient().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedActionException("You are not allowed to update this application");
        }

        if (request.status() == ApplicationStatus.PENDING) {
            throw new InvalidApplicationStatusException("Application status can only be updated to ACCEPTED or REJECTED");
        }

        application.setStatus(request.status());

        JobApplication savedApplication = jobApplicationRepository.save(application);

        return mapToApplicationResponse(savedApplication);
    }
}