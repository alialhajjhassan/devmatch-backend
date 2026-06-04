package com.example.devmatch.controller;



import com.example.devmatch.dto.CreateJobRequest;
import com.example.devmatch.dto.JobResponse;
import com.example.devmatch.dto.PagedResponse;
import com.example.devmatch.dto.UpdateJobRequest;
import com.example.devmatch.model.JobStatus;
import com.example.devmatch.service.JobPostingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Job Postings", description = "Endpoints for creating, reading, updating and deleting job postings")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    public JobPostingController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    @Operation(summary = "Create job posting", description = "Creates a new job posting. Requires CLIENT role.")
    @PostMapping
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody CreateJobRequest request) {
        JobResponse createdJob = jobPostingService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    @Operation(summary = "Get job postings", description = "Returns paginated, sorted and filtered job postings")
    @GetMapping
    public ResponseEntity<PagedResponse<JobResponse>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) JobStatus status,
            @RequestParam(required = false) String title
    ) {
        PagedResponse<JobResponse> response = jobPostingService.getAllJobs(
                page,
                size,
                sortBy,
                direction,
                status,
                title
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get job by id", description = "Returns a single job posting by id")
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return jobPostingService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get job by id", description = "Returns a single job posting by id")
    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody UpdateJobRequest request
    ) {
        return jobPostingService.updateJob(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Delete job posting", description = "Deletes a job posting. Only the job owner can delete it.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        boolean deleted = jobPostingService.deleteJob(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}