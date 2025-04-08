package com.springboot.domesticworkregistry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.Address;

public interface AddressRepository extends JpaRepository<Address, String> {

}
