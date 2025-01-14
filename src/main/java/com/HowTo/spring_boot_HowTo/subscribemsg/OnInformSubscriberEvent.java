package com.HowTo.spring_boot_HowTo.subscribemsg;

import org.springframework.context.ApplicationEvent;

import com.HowTo.spring_boot_HowTo.model.User;

@SuppressWarnings("serial")
public class OnInformSubscriberEvent extends ApplicationEvent {

    private final User user;
    private final String channelname;
    private final String tutorialtitle;


	public OnInformSubscriberEvent(final User user, final String channelname, final String tutorialtitle) {
        super(user);
        this.user = user;
        this.channelname = channelname;
        this.tutorialtitle = tutorialtitle;
    }

    //


	public User getUser() {
        return user;
    }


    public String getTutorialtitle() {
		return tutorialtitle;
	}

	public String getChannelname() {
		return channelname;
	}
}