package com.HowTo.spring_boot_HowTo.changepassword.listener;

import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.HowTo.spring_boot_HowTo.changepassword.OnChangePasswordEvent;
import com.HowTo.spring_boot_HowTo.model.User;

import com.HowTo.spring_boot_HowTo.service.UserServiceI;

@Component
public class ChangePasswordListener implements ApplicationListener<OnChangePasswordEvent> {
    @Autowired
    private UserServiceI service;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;


    // API

    @Override
    public void onApplicationEvent(OnChangePasswordEvent event) {
        this.confirmPassword(event);
    }

    private void confirmPassword(OnChangePasswordEvent event) {
        User test = event.getUser();
        User user = service.getUserById(test.getUserId());
        String token = user.getVerificationToken().getToken();
        service.getVerificationToken(token);
        
        String recipientAddress = user.getEmail();
        String subject = "Password change";
        String confirmationUrl 
          = "http://localhost:8080" + event.getAppUrl() + "/confirmPassword?token=" + token;
        String message = messages.getMessage("message.pwforgotten", null, event.getLocale());
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(email);
    }
}
