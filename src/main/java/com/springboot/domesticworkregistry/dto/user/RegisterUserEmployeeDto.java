package com.springboot.domesticworkregistry.dto.user;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserEmployeeDto {

    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String firstName;

    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String lastName;

    @NotNull(message = "is required")
    @Email
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "is required")
    private LocalDate birthDate;

    @NotNull(message = "is required")
    private String identificationNumber;

    @Size(min = 1)
    private String phone;

    @NotNull(message = "is required")
    private String street;

    @NotNull(message = "is required")
    private String number;

    private String apartment;

    @NotNull(message = "is required")
    private String city;

    @NotNull(message = "is required")
    private String postalCode;

    @NotNull(message = "is required")
    private String country;
}
