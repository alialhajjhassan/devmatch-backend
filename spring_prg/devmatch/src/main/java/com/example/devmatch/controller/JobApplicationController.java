package com.example.devmatch.controller;

import com.example.devmatch.dto.ApplicationResponse;
import com.example.devmatch.dto.CreateApplicationRequest;
import com.example.devmatch.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs/{jobId}/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> applyToJob(
            @PathVariable Long jobId,
            @Valid @RequestBody CreateApplicationRequest request
    ) {
        ApplicationResponse response = jobApplicationService.applyToJob(jobId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}