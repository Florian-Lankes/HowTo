package com.HowTo.spring_boot_HowTo.changepasswordloggedin;

import org.springframework.context.ApplicationEvent;

import com.HowTo.spring_boot_HowTo.model.User;

@SuppressWarnings("serial")
public class OnChangePasswordLoggedInEvent extends ApplicationEvent {

    private final User user;

    public OnChangePasswordLoggedInEvent(final User user) {
        super(user);
        this.user = user;
    }

    //


    public User getUser() {
        return user;
    }

}