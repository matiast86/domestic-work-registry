package com.springboot.domesticworkregistry.service.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.UserRepository;
import com.springboot.domesticworkregistry.dto.email.EmailDto;
import com.springboot.domesticworkregistry.dto.user.ChangePasswordDto;
import com.springboot.domesticworkregistry.dto.user.RegisterUserDto;
import com.springboot.domesticworkregistry.dto.user.RegisterUserEmployeeDto;
import com.springboot.domesticworkregistry.dto.user.ResetPasswordDto;
import com.springboot.domesticworkregistry.dto.user.UpdateUserDto;
import com.springboot.domesticworkregistry.entities.Address;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.User;
import com.springboot.domesticworkregistry.enums.Role;
import com.springboot.domesticworkregistry.exceptions.EmailAlreadyExistsException;
import com.springboot.domesticworkregistry.exceptions.EntityNotFoundException;
import com.springboot.domesticworkregistry.mapper.UserMapper;
import com.springboot.domesticworkregistry.service.email.EmailService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final EmailService emailService;

    @Value("${APP_BASE_URL}")
    private String baseUrl;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper mapper,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.emailService = emailService;
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
        newUser.setRoles(Set.of(Role.EMPLOYER));
        newUser.setFirstLogin(false);
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
        newUser.setRoles(Set.of(Role.EMPLOYEE));
        newUser.setPassword(passwordEncoder.encode(form.getIdentificationNumber()));
        newUser.setFirstLogin(true);
        newUser.setEmail(form.getEmail().toLowerCase());
        Address address = new Address(form.getStreet(), form.getNumber(), form.getApartment(), form.getCity(),
                form.getPostalCode(),
                form.getCountry());
        newUser.setAddress(address);
        return userRepository.save(newUser);
    }

    @Override
    public void changePassword(User user, ChangePasswordDto form) {
        // 1. Check that new password and confirmation match
        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect current password");
        }

        if (user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"))) {
            user.setFirstLogin(false);
        }

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);

    }

    @Override
    public void resetPasswordConfirmation(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        String changePasswordUrl = baseUrl + "/register/reset-password?token=" + token;

        EmailDto emailDto = new EmailDto();
        emailDto.setTo(List.of(email));
        emailDto.setSubject("Solicitud de cambio de contraseña");

        System.out.println("Reset Password link: " + changePasswordUrl);

        // emailService.changePasswordRequest(emailDto, user.getFirstName(),
        // changePasswordUrl);
    }

    @Override
    public void resetPassword(String token, ResetPasswordDto form) {
        User user = userRepository.findByResetToken(token)
                .filter(u -> u.getResetTokenExpiry().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new EntityNotFoundException("Token inválido o expirado"));
        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
        EmailDto dto = new EmailDto();
        dto.setTo(List.of(user.getEmail()));
        dto.setSubject("Cambio de contraseña");

        System.out.println("Contraseña cambiada con exito");

        // emailService.changePasswordConfirmation(dto, user.getFirstName());
    }

}
