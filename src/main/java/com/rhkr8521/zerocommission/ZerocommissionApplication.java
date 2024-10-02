package com.rhkr8521.zerocommission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ZerocommissionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZerocommissionApplication.class, args);
	}

}
