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
    private final CurrentUserService currentUserService;
    public JobPostingService(JobPostingRepository jobPostingRepository, CurrentUserService currentUserService) {
        this.jobPostingRepository = jobPostingRepository;
        this.currentUserService = currentUserService;
    }

    public JobResponse createJob(CreateJobRequest request) {

        User authenticatedUser = currentUserService.getAuthenticatedUser();

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
                jobPosting.getUpdatedAt(),
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

        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be negative");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }

        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("Sort direction must be either asc or desc");
        }

        validateSortBy(sortBy);


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
        User authenticatedUser = currentUserService.getAuthenticatedUser();
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
        User authenticatedUser = currentUserService.getAuthenticatedUser();
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


    private void validateSortBy(String sortBy) {
        List<String> allowedSortFields = List.of("id", "title", "budget", "status", "createdAt");

        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }
    }
}
