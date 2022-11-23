package com.techstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {
		"com.techstore.configuration",
		"com.techstore.controller",
		"com.techstore.service",
		"com.techstore.exception.handler"
})
@EntityScan(basePackages = "com.techstore.model.entity")
@EnableJpaRepositories(basePackages = "com.techstore.repository")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class TechStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechStoreApplication.class, args);
	}

}

