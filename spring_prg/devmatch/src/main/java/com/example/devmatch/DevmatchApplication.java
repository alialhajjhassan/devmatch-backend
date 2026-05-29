package com.example.devmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DevmatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevmatchApplication.class, args);
	}

}
