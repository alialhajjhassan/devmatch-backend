package com.example.devmatch.integration;

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
class JobPostingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @BeforeEach
    void cleanDatabase() {
        jobPostingRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createJob_shouldReturnCreated_whenUserIsClientAndTokenIsValid() throws Exception {
        String registerBody = """
                {
                  "username": "client_job_test",
                  "email": "clientjobtest@example.com",
                  "password": "password123",
                  "role": "CLIENT"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isOk());

        String loginBody = """
                {
                  "email": "clientjobtest@example.com",
                  "password": "password123"
                }
                """;

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = extractToken(loginResponse);

        String createJobBody = """
                {
                  "title": "Build integration test landing page",
                  "description": "I need a landing page created during an integration test.",
                  "budget": 500
                }
                """;

        mockMvc.perform(post("/api/jobs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJobBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title").value("Build integration test landing page"))
                .andExpect(jsonPath("$.budget").value(500))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.clientUsername").value("client_job_test"));
    }

    @Test
    void createJob_shouldReturnForbidden_whenUserIsFreelancer() throws Exception {
        String registerBody = """
                {
                  "username": "freelancer_job_test",
                  "email": "freelancerjobtest@example.com",
                  "password": "password123",
                  "role": "FREELANCER"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isOk());

        String loginBody = """
                {
                  "email": "freelancerjobtest@example.com",
                  "password": "password123"
                }
                """;

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = extractToken(loginResponse);

        String createJobBody = """
                {
                  "title": "This should fail",
                  "description": "Freelancer should not create job postings.",
                  "budget": 300
                }
                """;

        mockMvc.perform(post("/api/jobs")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJobBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("Only CLIENT users can create job postings"));
    }

    private String extractToken(String json) {
        String prefix = "\"token\":\"";
        int start = json.indexOf(prefix) + prefix.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}