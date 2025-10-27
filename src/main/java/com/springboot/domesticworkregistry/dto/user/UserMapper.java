package com.springboot.domesticworkregistry.dto.user;

import org.springframework.stereotype.Component;

import com.springboot.domesticworkregistry.entities.Address;
import com.springboot.domesticworkregistry.entities.User;

@Component
public class UserMapper {

    public User toEmployer(RegisterUserDto form) {
        User employer = new User();
        employer.setFirstName(form.getFirstName());
        employer.setLastName(form.getLastName());
        employer.setEmail(form.getEmail().toLowerCase());
        employer.setBirthDate(form.getBirthDate());
        employer.setIdentificationNumber(form.getIdentificationNumber());
        employer.setPhone(form.getPhone());

        Address address = new Address();
        address.setStreet(form.getStreet());
        address.setNumber(form.getNumber());
        address.setApartment(form.getApartment());
        address.setState(form.getState());
        address.setCity(form.getCity());
        address.setPostalCode(form.getPostalCode());
        address.setCountry(form.getCountry());

        employer.setAddress(address);
        return employer;
    }

    public User toEmployee(RegisterUserEmployeeDto form) {
        User employee = new User();
        employee.setFirstName(form.getFirstName());
        employee.setLastName(form.getLastName());
        employee.setEmail(form.getEmail().toLowerCase());
        employee.setBirthDate(form.getBirthDate());
        employee.setIdentificationNumber(form.getIdentificationNumber());
        employee.setPhone(form.getPhone());

        Address address = new Address();
        address.setStreet(form.getStreet());
        address.setNumber(form.getNumber());
        address.setApartment(form.getApartment());
        address.setState(form.getState());
        address.setCity(form.getCity());
        address.setPostalCode(form.getPostalCode());
        address.setCountry(form.getCountry());

        employee.setAddress(address);
        return employee;
    }

}
