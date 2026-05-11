package com.example.devmatch.service;


import com.example.devmatch.dto.CreateJobRequest;
import com.example.devmatch.dto.JobResponse;
import com.example.devmatch.dto.UpdateJobRequest;
import com.example.devmatch.model.JobPosting;
import com.example.devmatch.repository.JobPostingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    public JobPostingService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    public JobResponse createJob(CreateJobRequest request) {
        JobPosting jobPosting = new JobPosting();

        jobPosting.setTitle(request.title());
        jobPosting.setDescription(request.description());
        jobPosting.setBudget(request.budget());

        JobPosting savedJob = jobPostingRepository.save(jobPosting);
        return mapToJobResponse(savedJob);
    }

    private JobResponse mapToJobResponse(JobPosting jobPosting) {
        return new JobResponse(
                jobPosting.getId(),
                jobPosting.getTitle(),
                jobPosting.getDescription(),
                jobPosting.getBudget(),
                jobPosting.getStatus(),
                jobPosting.getCreatedAt()
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
