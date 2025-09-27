package com.springboot.domesticworkregistry.service.user;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.UserRepository;
import com.springboot.domesticworkregistry.dto.user.RegisterUserDto;
import com.springboot.domesticworkregistry.dto.user.RegisterUserEmployeeDto;
import com.springboot.domesticworkregistry.dto.user.UpdateUserDto;
import com.springboot.domesticworkregistry.entities.Address;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.User;
import com.springboot.domesticworkregistry.enums.Role;
import com.springboot.domesticworkregistry.exceptions.EmailAlreadyExistsException;
import com.springboot.domesticworkregistry.exceptions.EntityNotFoundException;
import com.springboot.domesticworkregistry.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public UpdateUserDto getUserDto(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        Address address = user.getAddress();
        UpdateUserDto dto = new UpdateUserDto();
        dto.setUserId(id);
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setBirthDate(user.getBirthDate());
        dto.setEmail(user.getEmail());
        dto.setIdentificationNumber(user.getIdentificationNumber());
        dto.setBirthDate(user.getBirthDate());
        dto.setPhone(user.getPhone());
        dto.setStreet(address.getStreet());
        dto.setNumber(address.getNumber());
        dto.setApartment(address.getApartment());
        dto.setCity(address.getCity());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());

        return dto;
    }

    @Override
    public User update(UpdateUserDto form, String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        Address address = user.getAddress();
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setEmail(form.getEmail());
        user.setIdentificationNumber(form.getIdentificationNumber());
        user.setBirthDate(form.getBirthDate());

        address.setStreet(form.getStreet());
        address.setNumber(form.getNumber());
        address.setApartment(form.getApartment());
        address.setCity(form.getCity());
        address.setPostalCode(form.getPostalCode());
        address.setCountry(form.getCountry());

        return userRepository.save(user);

    }

    @Override
    public void delete(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public User registerEmployer(RegisterUserDto form) {
        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered.");
        }

        User newUser = mapper.toEmployer(form);
        newUser.setPassword(passwordEncoder.encode(form.getPassword()));
        newUser.setEmail(form.getEmail().toLowerCase());
        Set<Role> roles = newUser.getRoles();
        newUser.setFirstLogin(false);
        roles.add(Role.EMPLOYER);
        Address address = new Address(form.getStreet(), form.getNumber(), form.getApartment(), form.getCity(),
                form.getPostalCode(),
                form.getCountry());
        newUser.setAddress(address);

        return userRepository.save(newUser);
    }

    @Override
    public List<Contract> findContractsByEmployer(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        return user.getEmployerContracts();
    }

    @Override
    public List<Contract> findContractsByEmployee(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        return user.getEmployeeContracts();
    }

    @Override
    public User registerEmployee(RegisterUserEmployeeDto form) {
        User newUser = mapper.toEmployee(form);
        newUser.getRoles().add(Role.EMPLOYEE);
        newUser.setPassword(passwordEncoder.encode(form.getIdentificationNumber()));
        newUser.setFirstLogin(true);
        newUser.setEmail(form.getEmail().toLowerCase());
        Address address = new Address(form.getStreet(), form.getNumber(), form.getApartment(), form.getCity(),
                form.getPostalCode(),
                form.getCountry());
        newUser.setAddress(address);
        return userRepository.save(newUser);
    }

}
