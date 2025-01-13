package com.HowTo.spring_boot_HowTo.changepasswordloggedin.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.HowTo.spring_boot_HowTo.changepasswordloggedin.OnChangePasswordLoggedInEvent;
import com.HowTo.spring_boot_HowTo.model.User;

import com.HowTo.spring_boot_HowTo.service.UserServiceI;

@Component
public class ChangePasswordLoggedInListener implements ApplicationListener<OnChangePasswordLoggedInEvent> {
    @Autowired
    private UserServiceI service;


    @Autowired
    private JavaMailSender mailSender;


    // API

    @Override
    public void onApplicationEvent(OnChangePasswordLoggedInEvent event) {
        this.changedPassword(event);
    }

    private void changedPassword(OnChangePasswordLoggedInEvent event) {
        User test = event.getUser();
        User user = service.getUserById(test.getUserId());
        
        String recipientAddress = user.getEmail();
        String subject = "Password change";
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Your password just got changed!");
        mailSender.send(email);
    }
}
