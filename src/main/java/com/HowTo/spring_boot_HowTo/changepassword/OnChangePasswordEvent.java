package com.HowTo.spring_boot_HowTo.changepassword;

import java.util.Locale;


import org.springframework.context.ApplicationEvent;

import com.HowTo.spring_boot_HowTo.model.User;

@SuppressWarnings("serial")
public class OnChangePasswordEvent extends ApplicationEvent {

    private final String appUrl;
    private final Locale locale;
    private final User user;

    public OnChangePasswordEvent(final User user, final Locale locale, final String appUrl) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    //

    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public User getUser() {
        return user;
    }

}