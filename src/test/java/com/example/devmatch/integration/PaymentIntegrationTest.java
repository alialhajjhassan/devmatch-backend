package com.example.devmatch.integration;

import com.example.devmatch.repository.JobApplicationRepository;
import com.example.devmatch.repository.JobPostingRepository;
import com.example.devmatch.repository.PaymentRepository;
import com.example.devmatch.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void cleanDatabase() {
        paymentRepository.deleteAll();
        jobApplicationRepository.deleteAll();
        jobPostingRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createPayment_shouldReturnCreated_whenOwnerClientPaysAcceptedApplication() throws Exception {
        String clientToken = registerAndLogin(
                "client_payment_owner",
                "clientpaymentowner@example.com",
                "CLIENT"
        );

        Long jobId = createJob(clientToken);

        String freelancerToken = registerAndLogin(
                "freelancer_payment",
                "freelancerpayment@example.com",
                "FREELANCER"
        );

        Long applicationId = applyToJob(jobId, freelancerToken);

        acceptApplication(applicationId, clientToken);

        String paymentBody = """
                {
                  "amount": 800
                }
                """;

        mockMvc.perform(post("/api/applications/" + applicationId + "/payments")
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.applicationId").value(applicationId))
                .andExpect(jsonPath("$.jobId").value(jobId))
                .andExpect(jsonPath("$.jobTitle").value("Build a React landing page"))
                .andExpect(jsonPath("$.clientUsername").value("client_payment_owner"))
                .andExpect(jsonPath("$.freelancerUsername").value("freelancer_payment"))
                .andExpect(jsonPath("$.amount").value(800))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andExpect(jsonPath("$.updatedAt", notNullValue()));
    }

    @Test
    void createPayment_shouldReturnBadRequest_whenApplicationIsAlreadyPaid() throws Exception {
        String clientToken = registerAndLogin(
                "client_payment_duplicate",
                "clientpaymentduplicate@example.com",
                "CLIENT"
        );

        Long jobId = createJob(clientToken);

        String freelancerToken = registerAndLogin(
                "freelancer_payment_duplicate",
                "freelancerpaymentduplicate@example.com",
                "FREELANCER"
        );

        Long applicationId = applyToJob(jobId, freelancerToken);

        acceptApplication(applicationId, clientToken);

        String paymentBody = """
                {
                  "amount": 800
                }
                """;

        mockMvc.perform(post("/api/applications/" + applicationId + "/payments")
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentBody))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/applications/" + applicationId + "/payments")
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("This application has already been paid"));
    }

    @Test
    void createPayment_shouldReturnForbidden_whenUserIsFreelancer() throws Exception {
        String clientToken = registerAndLogin(
                "client_payment_freelancer_block",
                "clientpaymentfreelancerblock@example.com",
                "CLIENT"
        );

        Long jobId = createJob(clientToken);

        String freelancerToken = registerAndLogin(
                "freelancer_payment_blocked",
                "freelancerpaymentblocked@example.com",
                "FREELANCER"
        );

        Long applicationId = applyToJob(jobId, freelancerToken);

        acceptApplication(applicationId, clientToken);

        String paymentBody = """
                {
                  "amount": 800
                }
                """;

        mockMvc.perform(post("/api/applications/" + applicationId + "/payments")
                        .header("Authorization", "Bearer " + freelancerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("Only CLIENT users can create payments"));
    }

    @Test
    void createPayment_shouldReturnForbidden_whenClientIsNotJobOwner() throws Exception {
        String ownerClientToken = registerAndLogin(
                "client_payment_real_owner",
                "clientpaymentrealowner@example.com",
                "CLIENT"
        );

        Long jobId = createJob(ownerClientToken);

        String freelancerToken = registerAndLogin(
                "freelancer_payment_other_client",
                "freelancerpaymentotherclient@example.com",
                "FREELANCER"
        );

        Long applicationId = applyToJob(jobId, freelancerToken);

        acceptApplication(applicationId, ownerClientToken);

        String otherClientToken = registerAndLogin(
                "client_payment_other",
                "clientpaymentother@example.com",
                "CLIENT"
        );

        String paymentBody = """
                {
                  "amount": 800
                }
                """;

        mockMvc.perform(post("/api/applications/" + applicationId + "/payments")
                        .header("Authorization", "Bearer " + otherClientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("You are not allowed to pay for this application"));
    }

    @Test
    void createPayment_shouldReturnBadRequest_whenApplicationIsPending() throws Exception {
        String clientToken = registerAndLogin(
                "client_payment_pending",
                "clientpaymentpending@example.com",
                "CLIENT"
        );

        Long jobId = createJob(clientToken);

        String freelancerToken = registerAndLogin(
                "freelancer_payment_pending",
                "freelancerpaymentpending@example.com",
                "FREELANCER"
        );

        Long applicationId = applyToJob(jobId, freelancerToken);

        String paymentBody = """
                {
                  "amount": 800
                }
                """;

        mockMvc.perform(post("/api/applications/" + applicationId + "/payments")
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Only ACCEPTED applications can be paid"));
    }

    @Test
    void createPayment_shouldReturnNotFound_whenApplicationDoesNotExist() throws Exception {
        String clientToken = registerAndLogin(
                "client_payment_not_found",
                "clientpaymentnotfound@example.com",
                "CLIENT"
        );

        String paymentBody = """
                {
                  "amount": 800
                }
                """;

        mockMvc.perform(post("/api/applications/999/payments")
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Application not found with id: 999"));
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

    private Long applyToJob(Long jobId, String freelancerToken) throws Exception {
        String applicationBody = """
                {
                  "coverLetter": "Hi, I have experience building landing pages and REST APIs."
                }
                """;

        String applicationResponse = mockMvc.perform(post("/api/jobs/" + jobId + "/applications")
                        .header("Authorization", "Bearer " + freelancerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(applicationBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return extractLongField(applicationResponse, "id");
    }

    private void acceptApplication(Long applicationId, String clientToken) throws Exception {
        String updateStatusBody = """
                {
                  "status": "ACCEPTED"
                }
                """;

        mockMvc.perform(patch("/api/applications/" + applicationId + "/status")
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateStatusBody))
                .andExpect(status().isOk());
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