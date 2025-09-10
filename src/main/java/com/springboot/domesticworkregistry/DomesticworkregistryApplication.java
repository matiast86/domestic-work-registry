package com.springboot.domesticworkregistry;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.springboot.domesticworkregistry.dao.EmployerRepository;
import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.enums.Role;


@SpringBootApplication
public class DomesticworkregistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DomesticworkregistryApplication.class, args);
	}

	@Bean
	public CommandLineRunner setupDefaultUser(
			EmployerRepository employerRepository,
			PasswordEncoder passwordEncoder // 👈 ya está bien inyectado acá
	) {
		return args -> {
			
	
			
			
			if (employerRepository.findByEmail("employer@example.com").isEmpty()) {
				Employer employer = new Employer();
				employer.setFirstName("Matias");
				employer.setLastName("Tailler");
				employer.setEmail("employer@example.com");
				String encodedPassword = passwordEncoder.encode("Password123!");
				employer.setPassword(encodedPassword);
				employer.setRole(Role.EMPLOYER);
				employer.setPhone("123456789");
				employerRepository.save(employer);
			}
		};
	}

}
