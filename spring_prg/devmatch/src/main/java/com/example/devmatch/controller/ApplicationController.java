package com.example.devmatch.controller;

import com.example.devmatch.dto.ApplicationResponse;
import com.example.devmatch.dto.UpdateApplicationStatusRequest;
import com.example.devmatch.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final JobApplicationService jobApplicationService;

    public ApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        ApplicationResponse response = jobApplicationService.updateApplicationStatus(applicationId, request);
        return ResponseEntity.ok(response);
    }
}