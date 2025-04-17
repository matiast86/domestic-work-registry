package com.springboot.domesticworkregistry.dto.employer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEmployerDto {

    @NotNull(message = "is requied")
    @Size(min = 1, message = "is required")
    private String firstName;

    @NotNull(message = "is requied")
    @Size(min = 1, message = "is required")
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
    private int age;
    private String street;
    private String number;
    private String city;
    private String postalCode;
    private String country;

}
