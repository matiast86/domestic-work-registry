package com.springboot.domesticworkregistry.service.email;

import com.springboot.domesticworkregistry.dto.email.EmailDto;

public interface EmailService {

    public void sendSimpleMail(EmailDto emailDto);

    public void sendHtmlEmail(EmailDto emailDto);

}
