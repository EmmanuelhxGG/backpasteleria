package com.example.PasteleriaBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.PasteleriaBackend.config.CorsProperties;
import com.example.PasteleriaBackend.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties({CorsProperties.class, JwtProperties.class})
public class PasteleriaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PasteleriaBackendApplication.class, args);
	}

}
