package com.springboot.domesticworkregistry.validation;

import com.springboot.domesticworkregistry.dto.user.ChangePasswordDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidation implements ConstraintValidator<PasswordMatches, ChangePasswordDto> {

    @Override
    public boolean isValid(ChangePasswordDto dto, ConstraintValidatorContext context) {
        if (dto.getNewPassword() == null || dto.getRepeatNewPassword() == null) {
            return true;
        }
        return dto.getNewPassword().equals(dto.getRepeatNewPassword());
    }

}
