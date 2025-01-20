package com.HowTo.spring_boot_HowTo.registration.listener;

import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.registration.OnRegistrationCompleteEvent;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private UserServiceI service;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;


    // API

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    //Creates and sends a Email to user that registered with a link to enable the user
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);
        
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl 
          = "http://localhost:8080" + event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = messages.getMessage("message.regSuccLink", null, event.getLocale());
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(email);
    }
}
