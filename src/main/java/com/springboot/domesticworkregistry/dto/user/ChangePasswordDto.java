package com.springboot.domesticworkregistry.dto.user;

import com.springboot.domesticworkregistry.validation.PasswordMatches;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class ChangePasswordDto {

    @NotNull(message = "Current password is required")
    private String currentPassword;

    @NotNull(message = "New password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$", message = "Password must have at least 1 uppercase, 1 lowercase, 1 number, 1 special character, and be 8–16 characters long.")
    private String newPassword;

    @NotNull(message = "Please confirm the new password")
    private String repeatNewPassword;

}
