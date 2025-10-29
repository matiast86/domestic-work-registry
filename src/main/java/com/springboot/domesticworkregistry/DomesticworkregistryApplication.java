package com.springboot.domesticworkregistry;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class DomesticworkregistryApplication {

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.out.println("✅ Default timezone set to UTC");
	}

	public static void main(String[] args) {
		SpringApplication.run(DomesticworkregistryApplication.class, args);
	}

}
