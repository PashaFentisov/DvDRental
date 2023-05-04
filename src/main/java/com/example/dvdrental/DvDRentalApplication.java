package com.example.dvdrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"service", "controller", "repository"})
@EnableJpaRepositories("repository")
@EntityScan("entity")
public class DvDRentalApplication {
    public static void main(String[] args) {
        SpringApplication.run(DvDRentalApplication.class, args);
    }

}
