package com.springboot.domesticworkregistry.service.email;

import java.util.Map;

import com.springboot.domesticworkregistry.dto.email.EmailDto;

public interface EmailService {

    public void sendSimpleMail(EmailDto emailDto);

    public void sendTemplatedEmail(EmailDto emailDto, String templateName, Map<String, Object> variables);

    public void sendWelcomeEmail(EmailDto emailDto, String name, String loginUrl);

    public void changePasswordRequest(EmailDto emailDto, String name, String passwordUrl);

    public void changePasswordConfirmation(EmailDto emailDto, String name);

    public void sendWelcomeEmployerEmail(EmailDto emailDto, String name, String activationUrl);

    public void sendWelcomeEmployeeEmail(EmailDto emailDto, String employeeName, String employerName,
            String setupPasswordUrl);

    public void sendPasswordResetEmail(EmailDto emailDto, String name, String resetPasswordUrl);

    public void sendPasswordChangedConfirmation(EmailDto emailDto, String name);

    public void sendContractCreatedEmail(EmailDto emailDto, String employeeName, String employerName, String jobType,
            String startDate);

}
