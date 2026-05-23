package com.example.devmatch.integration;

import com.example.devmatch.repository.JobApplicationRepository;
import com.example.devmatch.repository.JobPostingRepository;
import com.example.devmatch.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class JobApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @BeforeEach
    void cleanDatabase() {
        jobApplicationRepository.deleteAll();
        jobPostingRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void applyToJob_shouldReturnCreated_whenUserIsFreelancer() throws Exception {
        String clientToken = registerAndLogin(
                "client_apply_test",
                "clientapplytest@example.com",
                "CLIENT"
        );

        Long jobId = createJob(clientToken);

        String freelancerToken = registerAndLogin(
                "freelancer_apply_test",
                "freelancerapplytest@example.com",
                "FREELANCER"
        );

        String applicationBody = """
                {
                  "coverLetter": "Hi, I have experience building landing pages and REST APIs."
                }
                """;

        mockMvc.perform(post("/api/jobs/" + jobId + "/applications")
                        .header("Authorization", "Bearer " + freelancerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(applicationBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.jobId").value(jobId))
                .andExpect(jsonPath("$.jobTitle").value("Build a React landing page"))
                .andExpect(jsonPath("$.freelancerUsername").value("freelancer_apply_test"))
                .andExpect(jsonPath("$.coverLetter").value("Hi, I have experience building landing pages and REST APIs."))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.createdAt", notNullValue()));
    }

    @Test
    void applyToJob_shouldReturnBadRequest_whenFreelancerAppliesTwice() throws Exception {
        String clientToken = registerAndLogin(
                "client_duplicate_test",
                "clientduplicatetest@example.com",
                "CLIENT"
        );

        Long jobId = createJob(clientToken);

        String freelancerToken = registerAndLogin(
                "freelancer_duplicate_test",
                "freelancerduplicatetest@example.com",
                "FREELANCER"
        );

        String applicationBody = """
                {
                  "coverLetter": "First application."
                }
                """;

        mockMvc.perform(post("/api/jobs/" + jobId + "/applications")
                        .header("Authorization", "Bearer " + freelancerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(applicationBody))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/jobs/" + jobId + "/applications")
                        .header("Authorization", "Bearer " + freelancerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(applicationBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("You have already applied to this job posting"));
    }

    @Test
    void applyToJob_shouldReturnForbidden_whenUserIsClient() throws Exception {
        String clientToken = registerAndLogin(
                "client_forbidden_test",
                "clientforbiddentest@example.com",
                "CLIENT"
        );

        Long jobId = createJob(clientToken);

        String applicationBody = """
                {
                  "coverLetter": "Client should not apply."
                }
                """;

        mockMvc.perform(post("/api/jobs/" + jobId + "/applications")
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(applicationBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("Only FREELANCER users can apply to job postings"));
    }

    @Test
    void applyToJob_shouldReturnNotFound_whenJobDoesNotExist() throws Exception {
        String freelancerToken = registerAndLogin(
                "freelancer_notfound_test",
                "freelancernotfoundtest@example.com",
                "FREELANCER"
        );

        String applicationBody = """
                {
                  "coverLetter": "Trying to apply to a missing job."
                }
                """;

        mockMvc.perform(post("/api/jobs/999/applications")
                        .header("Authorization", "Bearer " + freelancerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(applicationBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Job posting not found with id: 999"));
    }

    private String registerAndLogin(String username, String email, String role) throws Exception {
        String registerBody = """
                {
                  "username": "%s",
                  "email": "%s",
                  "password": "password123",
                  "role": "%s"
                }
                """.formatted(username, email, role);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isOk());

        String loginBody = """
                {
                  "email": "%s",
                  "password": "password123"
                }
                """.formatted(email);

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return extractToken(loginResponse);
    }

    private Long createJob(String clientToken) throws Exception {
        String createJobBody = """
                {
                  "title": "Build a React landing page",
                  "description": "I need a modern landing page for my SaaS product.",
                  "budget": 800
                }
                """;

        String jobResponse = mockMvc.perform(post("/api/jobs")
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJobBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return extractLongField(jobResponse, "id");
    }

    private String extractToken(String json) {
        String prefix = "\"token\":\"";
        int start = json.indexOf(prefix) + prefix.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private Long extractLongField(String json, String fieldName) {
        String prefix = "\"" + fieldName + "\":";
        int start = json.indexOf(prefix) + prefix.length();
        int end = json.indexOf(",", start);

        if (end == -1) {
            end = json.indexOf("}", start);
        }

        return Long.parseLong(json.substring(start, end).trim());
    }
}