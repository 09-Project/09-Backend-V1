package com.example.project09;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Project09Application {

    public static void main(String[] args) {
        SpringApplication.run(Project09Application.class, args);
    }

}
