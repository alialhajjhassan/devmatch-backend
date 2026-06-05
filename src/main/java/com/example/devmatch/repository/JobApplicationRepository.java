package com.example.devmatch.repository;

import com.example.devmatch.model.JobApplication;
import com.example.devmatch.model.JobPosting;
import com.example.devmatch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByJobPostingAndFreelancer(JobPosting jobPosting, User freelancer);

    List<JobApplication> findByJobPosting(JobPosting jobPosting);

    Optional<JobApplication> findByJobPostingAndFreelancer(JobPosting jobPosting, User freelancer);
}