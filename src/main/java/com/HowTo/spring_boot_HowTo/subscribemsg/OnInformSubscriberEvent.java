package com.HowTo.spring_boot_HowTo.subscribemsg;

import org.springframework.context.ApplicationEvent;

import com.HowTo.spring_boot_HowTo.model.User;

@SuppressWarnings("serial")
public class OnInformSubscriberEvent extends ApplicationEvent {

    private final User user;
    private final String channelname;


	public OnInformSubscriberEvent(final User user, final String channelname) {
        super(user);
        this.user = user;
        this.channelname = channelname;
    }

    //


    public User getUser() {
        return user;
    }


    public String getChannelname() {
		return channelname;
	}
}