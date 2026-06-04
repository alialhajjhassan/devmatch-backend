package com.example.devmatch.controller;

import com.example.devmatch.dto.ApplicationResponse;
import com.example.devmatch.dto.CreateApplicationRequest;
import com.example.devmatch.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/jobs/{jobId}/applications")
@Tag(name = "Job Applications", description = "Endpoints for freelancers applying to job postings")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @Operation(summary = "Apply to job", description = "Allows a FREELANCER to apply to a job posting")
    @PostMapping
    public ResponseEntity<ApplicationResponse> applyToJob(
            @PathVariable Long jobId,
            @Valid @RequestBody CreateApplicationRequest request
    ) {
        ApplicationResponse response = jobApplicationService.applyToJob(jobId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}