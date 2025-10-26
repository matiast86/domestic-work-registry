package com.springboot.domesticworkregistry.controller.email;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.domesticworkregistry.dto.email.EmailDto;
import com.springboot.domesticworkregistry.service.email.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@Valid @RequestBody EmailDto dto) {
        try {
            emailService.sendSimpleMail(dto);
            return ResponseEntity.ok("✅ Email sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("❌ Error sending email: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> sendTestEmail() {
        try {
            List<String> to = new ArrayList<>();
            to.add("domesticworkregistry.test@gmail.com");

            EmailDto testEmail = new EmailDto();
            testEmail.setTo(to); // 👈 your test recipient
            testEmail.setSubject("Test Email from Domestic Work Registry");
            testEmail.setText("✅ This is a test email sent successfully from Spring Boot!");

            emailService.sendSimpleMail(testEmail);

            return ResponseEntity.ok("✅ Test email sent successfully! Check your inbox.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("❌ Failed to send test email: " + e.getMessage());
        }
    }
}
