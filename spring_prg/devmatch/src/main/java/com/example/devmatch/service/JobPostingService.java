package com.example.devmatch.service;


import com.example.devmatch.dto.CreateJobRequest;
import com.example.devmatch.dto.JobResponse;
import com.example.devmatch.dto.UpdateJobRequest;
import com.example.devmatch.exception.ResourceNotFoundException;
import com.example.devmatch.model.JobPosting;
import com.example.devmatch.model.Role;
import com.example.devmatch.model.User;
import com.example.devmatch.repository.JobPostingRepository;
import com.example.devmatch.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;
    public JobPostingService(JobPostingRepository jobPostingRepository, UserRepository userRepository) {
        this.jobPostingRepository = jobPostingRepository;
        this.userRepository = userRepository;
    }

    public JobResponse createJob(CreateJobRequest request) {
        User client = userRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + request.clientId()));

        if (client.getRole() != Role.CLIENT) {
            throw new IllegalArgumentException("Only CLIENT users can create job postings");
        }

        JobPosting jobPosting = new JobPosting();

        jobPosting.setTitle(request.title());
        jobPosting.setDescription(request.description());
        jobPosting.setBudget(request.budget());
        jobPosting.setClient(client);

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

    public List<JobResponse> getAllJobs() {
        return jobPostingRepository.findAll()
                .stream()
                .map(this::mapToJobResponse)
                .toList();
    }

    public Optional<JobResponse> getJobById(Long id) {
        return jobPostingRepository.findById(id)
                .map(this::mapToJobResponse);
    }

    public Optional<JobResponse> updateJob(Long id, UpdateJobRequest request) {
        return jobPostingRepository.findById(id)
                .map(existingJob -> {
                    existingJob.setTitle(request.title());
                    existingJob.setDescription(request.description());
                    existingJob.setBudget(request.budget());
                    existingJob.setStatus(request.status());
                    JobPosting savedJob = jobPostingRepository.save(existingJob);

                    return mapToJobResponse(savedJob);
                });
    }

    public boolean deleteJob(Long id) {
        if (!jobPostingRepository.existsById(id)) {
            return false;
        }

        jobPostingRepository.deleteById(id);
        return true;
    }
}
