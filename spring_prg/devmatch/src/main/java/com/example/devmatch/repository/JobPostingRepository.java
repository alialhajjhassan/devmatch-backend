package com.example.devmatch.repository;

import com.example.devmatch.model.JobPosting;
import com.example.devmatch.model.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    Page<JobPosting> findByStatus(JobStatus status, Pageable pageable);

    Page<JobPosting> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<JobPosting> findByStatusAndTitleContainingIgnoreCase(
            JobStatus status,
            String title,
            Pageable pageable
    );
}
