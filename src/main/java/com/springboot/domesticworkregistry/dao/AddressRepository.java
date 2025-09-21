package com.springboot.domesticworkregistry.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

}
