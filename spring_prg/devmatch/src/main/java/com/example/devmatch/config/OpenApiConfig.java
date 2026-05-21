package com.example.devmatch.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI devMatchOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DevMatch API")
                        .description("REST API for a freelance marketplace built with Spring Boot")
                        .version("v1.0.0"));
    }
}