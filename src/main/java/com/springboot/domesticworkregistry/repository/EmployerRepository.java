package com.springboot.domesticworkregistry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.Employer;

public interface EmployerRepository extends JpaRepository<Employer, String> {

    Optional<Employer> findByEmail(String email);

}
