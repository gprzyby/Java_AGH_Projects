package edu.gprzyb.lab11_hibernate_final;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class Lab11HibernateApplication {

	public static void main(String[] args) {
		SpringApplication.run(Lab11HibernateApplication.class, args);
	}

}
