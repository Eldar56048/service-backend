package com.crm.servicebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class ServiceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceBackendApplication.class, args);
	}

}
