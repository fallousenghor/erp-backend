package com.ceremony.ceremony_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // Pour les emails asynchrones
public class CeremonyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CeremonyBackendApplication.class, args);
	}

}
