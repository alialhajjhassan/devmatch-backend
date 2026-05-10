package com.example.devmatch.service;


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

    public JobPosting createJob(JobPosting jobPosting) {
        return jobPostingRepository.save(jobPosting);
    }

    public List<JobPosting> getAllJobs() {
        return jobPostingRepository.findAll();
    }

    public Optional<JobPosting> getJobById(Long id) {
        return jobPostingRepository.findById(id);
    }

    public Optional<JobPosting> updateJob(Long id, JobPosting updatedJob) {
        return jobPostingRepository.findById(id)
                .map(existingJob -> {
                    existingJob.setTitle(updatedJob.getTitle());
                    existingJob.setDescription(updatedJob.getDescription());
                    existingJob.setBudget(updatedJob.getBudget());
                    existingJob.setStatus(updatedJob.getStatus());
                    return jobPostingRepository.save(existingJob);
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
