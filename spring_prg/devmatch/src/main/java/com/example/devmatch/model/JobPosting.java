package com.example.devmatch.model;

import com.example.devmatch.model.audit.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_postings")
@Getter
@Setter
@NoArgsConstructor

public class JobPosting extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Column(nullable = false)
    private String title;


    @NotBlank(message = "Description is mandatory")
    @Column(nullable = false, length = 2000)
    private String description;

    @NotNull(message = "Budget is mandatory")
    @Positive(message = "Budget must be greater than zero")
    @Column(nullable = false)
    private BigDecimal budget;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> applications = new ArrayList<>();

}
