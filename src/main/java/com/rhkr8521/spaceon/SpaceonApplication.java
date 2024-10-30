package com.rhkr8521.spaceon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpaceonApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpaceonApplication.class, args);
	}

}
