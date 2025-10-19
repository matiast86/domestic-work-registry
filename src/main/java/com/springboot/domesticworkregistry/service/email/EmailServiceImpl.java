package com.springboot.domesticworkregistry.service.email;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.springboot.domesticworkregistry.dto.email.EmailDto;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendSimpleMail(EmailDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(emailDto.getTo().toArray(new String[0]));
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getText());
        mailSender.send(message);
    }

    @Override
    public void sendTemplatedEmail(EmailDto emailDto, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(from);
            helper.setTo(emailDto.getTo().toArray(new String[0]));
            helper.setSubject(emailDto.getSubject());

            Context context = new Context();
            context.setVariables(variables);

            String html = templateEngine.process(templateName, context);
            helper.setText(html, true);
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendWelcomeEmail(EmailDto emailDto, String name, String loginUrl) {
        sendTemplatedEmail(emailDto, "emails/welcome", Map.of("name", name, "loginUrl", loginUrl));
    }

}
