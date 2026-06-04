package com.example.devmatch.controller;

import com.example.devmatch.dto.ApplicationResponse;
import com.example.devmatch.dto.UpdateApplicationStatusRequest;
import com.example.devmatch.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/applications")
@Tag(name = "Applications", description = "Endpoints for managing application status")
public class ApplicationController {

    private final JobApplicationService jobApplicationService;

    public ApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @Operation(summary = "Update application status", description = "Allows the job owner to accept or reject an application")
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        ApplicationResponse response = jobApplicationService.updateApplicationStatus(applicationId, request);
        return ResponseEntity.ok(response);
    }
}