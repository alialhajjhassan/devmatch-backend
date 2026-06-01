package com.example.devmatch.repository;

import com.example.devmatch.model.JobApplication;
import com.example.devmatch.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByApplication(JobApplication application);
}