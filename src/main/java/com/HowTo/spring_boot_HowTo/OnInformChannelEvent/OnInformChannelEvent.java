package com.HowTo.spring_boot_HowTo.OnInformChannelEvent;

import org.springframework.context.ApplicationEvent;

import com.HowTo.spring_boot_HowTo.model.User;

@SuppressWarnings("serial")
public class OnInformChannelEvent extends ApplicationEvent {

    private final User user;
    private final String username;



	public OnInformChannelEvent(final User user, final String username) {
        super(user);
        this.user = user;
        this.username = username;
    }

    //


    public User getUser() {
        return user;
    }


	public String getUsername() {
		return username;
	}

}