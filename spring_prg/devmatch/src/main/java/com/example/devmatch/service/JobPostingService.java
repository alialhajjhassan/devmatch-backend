package com.example.devmatch.service;


import com.example.devmatch.dto.CreateJobRequest;
import com.example.devmatch.dto.JobResponse;
import com.example.devmatch.dto.UpdateJobRequest;
import com.example.devmatch.exception.UnauthorizedActionException;
import com.example.devmatch.model.JobPosting;
import com.example.devmatch.model.JobStatus;
import com.example.devmatch.model.Role;
import com.example.devmatch.model.User;
import com.example.devmatch.repository.JobPostingRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.devmatch.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;



import java.util.List;
import java.util.Optional;

@Service
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    public JobPostingService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    public JobResponse createJob(CreateJobRequest request) {

        User authenticatedUser = getAuthenticatedUser();

        if (authenticatedUser.getRole() != Role.CLIENT) {
            throw new UnauthorizedActionException("Only CLIENT users can create job postings");
        }

        JobPosting jobPosting = new JobPosting();

        jobPosting.setTitle(request.title());
        jobPosting.setDescription(request.description());
        jobPosting.setBudget(request.budget());
        jobPosting.setClient(authenticatedUser);

        JobPosting savedJob = jobPostingRepository.save(jobPosting);
        return mapToJobResponse(savedJob);
    }

    private JobResponse mapToJobResponse(JobPosting jobPosting) {
        User client = jobPosting.getClient();
        return new JobResponse(
                jobPosting.getId(),
                jobPosting.getTitle(),
                jobPosting.getDescription(),
                jobPosting.getBudget(),
                jobPosting.getStatus(),
                jobPosting.getCreatedAt(),
                client.getId(),
                client.getUsername()
        );
    }


    public PagedResponse<JobResponse> getAllJobs(
            int page,
            int size,
            String sortBy,
            String direction,
            JobStatus status,
            String title
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<JobPosting> jobPage;

        if (status != null && title != null && !title.isBlank()) {
            jobPage = jobPostingRepository.findByStatusAndTitleContainingIgnoreCase(status, title, pageable);
        } else if (status != null) {
            jobPage = jobPostingRepository.findByStatus(status, pageable);
        } else if (title != null && !title.isBlank()) {
            jobPage = jobPostingRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else {
            jobPage = jobPostingRepository.findAll(pageable);
        }

        List<JobResponse> content = jobPage.getContent()
                .stream()
                .map(this::mapToJobResponse)
                .toList();

        return new PagedResponse<>(
                content,
                jobPage.getNumber(),
                jobPage.getSize(),
                jobPage.getTotalElements(),
                jobPage.getTotalPages(),
                jobPage.isLast()
        );
    }

    public Optional<JobResponse> getJobById(Long id) {
        return jobPostingRepository.findById(id)
                .map(this::mapToJobResponse);
    }

    public Optional<JobResponse> updateJob(Long id, UpdateJobRequest request) {
        User authenticatedUser = getAuthenticatedUser();
        return jobPostingRepository.findById(id)
                .map(existingJob -> {
                    validateJobOwnership(existingJob, authenticatedUser);
                    existingJob.setTitle(request.title());
                    existingJob.setDescription(request.description());
                    existingJob.setBudget(request.budget());
                    existingJob.setStatus(request.status());
                    JobPosting savedJob = jobPostingRepository.save(existingJob);

                    return mapToJobResponse(savedJob);
                });
    }

    public boolean deleteJob(Long id) {
        User authenticatedUser = getAuthenticatedUser();
        Optional<JobPosting> jobOptional = jobPostingRepository.findById(id);

        if (jobOptional.isEmpty()) {
            return false;
        }

        JobPosting jobPosting = jobOptional.get();

        validateJobOwnership(jobPosting, authenticatedUser);

        jobPostingRepository.delete(jobPosting);
        return true;
    }

    private void validateJobOwnership(JobPosting jobPosting, User authenticatedUser) {
        if (!jobPosting.getClient().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedActionException("You are not allowed to modify this job posting");
        }
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new UnauthorizedActionException("User is not authenticated");
        }

        return user;
    }
}
