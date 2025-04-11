package com.springboot.domesticworkregistry.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.Employer;

public interface EmployerRepository extends JpaRepository<Employer, String> {

    Optional<Employer> findByEmail(String email);

    Optional<Employer> findById(String id);

}
