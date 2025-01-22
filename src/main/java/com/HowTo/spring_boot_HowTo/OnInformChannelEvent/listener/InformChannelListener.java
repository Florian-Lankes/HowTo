package com.HowTo.spring_boot_HowTo.OnInformChannelEvent.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.HowTo.spring_boot_HowTo.OnInformChannelEvent.OnInformChannelEvent;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

@Component
public class InformChannelListener implements ApplicationListener<OnInformChannelEvent> {
    @Autowired
    private UserServiceI service;


    @Autowired
    private JavaMailSender mailSender;


    // API

    @Override
    public void onApplicationEvent(OnInformChannelEvent event) {
        this.sendmail(event);
    }

    private void sendmail(OnInformChannelEvent event) {
        User test = event.getUser();
        User user = service.getUserById(test.getUserId());
        
        String recipientAddress = user.getEmail();
        String subject = "Channel got subscribed";
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Congrats! Your Channel got a new subscriber: "+ event.getUsername());
        mailSender.send(email);
    }
}
