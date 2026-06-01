package com.example.devmatch.service;

import com.example.devmatch.dto.CreatePaymentRequest;
import com.example.devmatch.dto.PaymentResponse;
import com.example.devmatch.exception.ResourceNotFoundException;
import com.example.devmatch.exception.UnauthorizedActionException;
import com.example.devmatch.model.ApplicationStatus;
import com.example.devmatch.model.JobApplication;
import com.example.devmatch.model.JobPosting;
import com.example.devmatch.model.Payment;
import com.example.devmatch.model.PaymentStatus;
import com.example.devmatch.model.Role;
import com.example.devmatch.model.User;
import com.example.devmatch.repository.JobApplicationRepository;
import com.example.devmatch.repository.PaymentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            JobApplicationRepository jobApplicationRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public PaymentResponse createPayment(Long applicationId, CreatePaymentRequest request) {
        User authenticatedUser = getAuthenticatedUser();

        if (authenticatedUser.getRole() != Role.CLIENT) {
            throw new UnauthorizedActionException("Only CLIENT users can create payments");
        }

        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + applicationId
                ));

        JobPosting jobPosting = application.getJobPosting();
        User client = jobPosting.getClient();

        if (!client.getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedActionException("You are not allowed to pay for this application");
        }

        if (application.getStatus() != ApplicationStatus.ACCEPTED) {
            throw new IllegalArgumentException("Only ACCEPTED applications can be paid");
        }

        boolean alreadyPaid = paymentRepository.existsByApplication(application);

        if (alreadyPaid) {
            throw new IllegalArgumentException("This application has already been paid");
        }

        Payment payment = new Payment();
        payment.setApplication(application);
        payment.setAmount(request.amount());
        payment.setStatus(PaymentStatus.COMPLETED);

        Payment savedPayment = paymentRepository.save(payment);

        return mapToPaymentResponse(savedPayment);
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        JobApplication application = payment.getApplication();
        JobPosting jobPosting = application.getJobPosting();
        User client = jobPosting.getClient();
        User freelancer = application.getFreelancer();

        return new PaymentResponse(
                payment.getId(),
                application.getId(),
                jobPosting.getId(),
                jobPosting.getTitle(),
                client.getId(),
                client.getUsername(),
                freelancer.getId(),
                freelancer.getUsername(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new UnauthorizedActionException("User is not authenticated");
        }

        return user;
    }
}