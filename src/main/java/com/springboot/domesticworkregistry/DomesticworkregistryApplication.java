package com.springboot.domesticworkregistry;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springboot.domesticworkregistry.dao.UserRepository;
import com.springboot.domesticworkregistry.entities.Address;
import com.springboot.domesticworkregistry.entities.User;
import com.springboot.domesticworkregistry.enums.Role;

@SpringBootApplication
public class DomesticworkregistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DomesticworkregistryApplication.class, args);
	}

	@Bean
	public CommandLineRunner setupDefaultUser(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {

			if (userRepository.findByEmail("matias@mail.com").isEmpty()) {
				User employer = new User();
				employer.setFirstName("Matias");
				employer.setLastName("Tailler");
				employer.setEmail("matias@mail.com");
				String encodedPassword = passwordEncoder.encode("Password123!");
				employer.setPassword(encodedPassword);
				Set<Role> roles = new HashSet<>();
				roles.add(Role.EMPLOYER);
				employer.setBirthDate(LocalDate.of(1986, Month.JUNE, 19));
				employer.setIdentificationNumber("20324019007");
				employer.setPhone("1134584914");
				Address address = new Address();
				address.setStreet("Lavalle");
				address.setNumber("3972");
				address.setApartment("1C");
				address.setCity("CABA");
				address.setPostalCode("1190");
				address.setCountry("Argentina");
				employer.setAddress(address);
				userRepository.save(employer);
			}
		};
	}

}
