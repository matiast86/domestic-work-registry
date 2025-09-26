package com.springboot.domesticworkregistry.dto.user;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {
    @NotBlank(message = "is required")
    private String firstName;

    @NotBlank(message = "is required")
    private String lastName;

    @NotNull(message = "is requied")
    @Email
    private String email;

    @NotNull(message = "is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$", message = "Password should include al least one upper case, one lower case, a number, a special character and be between 8 and 16 characters.")
    private String password;

    @NotNull(message = "is required")
    private String repeatPassword;

    @NotNull(message = "is required")
    private String identificationNumber;

    @Size(min = 1)
    private String phone;

    private LocalDate birthDate;

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

    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordsMatching() {
        return password != null && password.equals(repeatPassword);
    }

}
