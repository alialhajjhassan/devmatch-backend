package com.example.devmatch.service;

import com.example.devmatch.dto.CreateJobRequest;
import com.example.devmatch.dto.JobResponse;
import com.example.devmatch.exception.UnauthorizedActionException;
import com.example.devmatch.model.JobPosting;
import com.example.devmatch.model.Role;
import com.example.devmatch.model.User;
import com.example.devmatch.repository.JobPostingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobPostingServiceTest {

    @Mock
    private JobPostingRepository jobPostingRepository;
    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private JobPostingService jobPostingService;


    @Test
    void createJob_shouldCreateJob_whenAuthenticatedUserIsClient() {
        User client = new User();
        client.setId(1L);
        client.setUsername("client_test");
        client.setEmail("client@test.com");
        client.setRole(Role.CLIENT);

        when(currentUserService.getAuthenticatedUser()).thenReturn(client);

        CreateJobRequest request = new CreateJobRequest(
                "Build landing page",
                "I need a responsive landing page.",
                BigDecimal.valueOf(500)
        );

        JobPosting savedJob = new JobPosting();
        savedJob.setId(1L);
        savedJob.setTitle("Build landing page");
        savedJob.setDescription("I need a responsive landing page.");
        savedJob.setBudget(BigDecimal.valueOf(500));
        savedJob.setClient(client);

        when(jobPostingRepository.save(any(JobPosting.class))).thenReturn(savedJob);

        JobResponse response = jobPostingService.createJob(request);

        assertEquals(1L, response.id());
        assertEquals("Build landing page", response.title());
        assertEquals(BigDecimal.valueOf(500), response.budget());
        assertEquals(1L, response.clientId());
        assertEquals("client_test", response.clientUsername());

        verify(currentUserService).getAuthenticatedUser();
        verify(jobPostingRepository).save(any(JobPosting.class));
    }

    @Test
    void createJob_shouldThrowUnauthorizedActionException_whenAuthenticatedUserIsFreelancer() {

        User freelancer = new User();
        freelancer.setId(2L);
        freelancer.setUsername("freelancer_test");
        freelancer.setEmail("freelancer@test.com");
        freelancer.setRole(Role.FREELANCER);

        when(currentUserService.getAuthenticatedUser()).thenReturn(freelancer);

        CreateJobRequest request = new CreateJobRequest(
                "This should fail",
                "Freelancer should not create jobs.",
                BigDecimal.valueOf(300)
        );


        assertThrows(UnauthorizedActionException.class, () -> jobPostingService.createJob(request));

        verify(currentUserService).getAuthenticatedUser();
        verify(jobPostingRepository, never()).save(any(JobPosting.class));
    }
}