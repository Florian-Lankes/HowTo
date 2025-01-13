package com.HowTo.spring_boot_HowTo.subscribemsg.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.HowTo.spring_boot_HowTo.changepasswordloggedin.OnChangePasswordLoggedInEvent;
import com.HowTo.spring_boot_HowTo.model.User;

import com.HowTo.spring_boot_HowTo.service.UserServiceI;
import com.HowTo.spring_boot_HowTo.subscribemsg.OnInformSubscriberEvent;

@Component
public class InformSubscriberListener implements ApplicationListener<OnInformSubscriberEvent> {
    @Autowired
    private UserServiceI service;


    @Autowired
    private JavaMailSender mailSender;


    // API

    @Override
    public void onApplicationEvent(OnInformSubscriberEvent event) {
        this.sendmail(event);
    }

    private void sendmail(OnInformSubscriberEvent event) {
        User test = event.getUser();
        User user = service.getUserById(test.getUserId());
        
        String recipientAddress = user.getEmail();
        String subject = "New Tutorial";
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("New tutorial from: "+ event.getChannelname() + "-" + event.getTutorialtitle());
        mailSender.send(email);
    }
}
