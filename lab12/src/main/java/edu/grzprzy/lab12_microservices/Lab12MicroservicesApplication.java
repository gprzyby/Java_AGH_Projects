package edu.grzprzy.lab12_microservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import java.util.Date;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "edu.grzprzy.lab12_microservices.repositories")
public class Lab12MicroservicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lab12MicroservicesApplication.class, args);
    }

}
