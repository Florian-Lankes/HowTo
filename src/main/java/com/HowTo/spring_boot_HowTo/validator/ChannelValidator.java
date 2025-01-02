package com.HowTo.spring_boot_HowTo.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.HowTo.spring_boot_HowTo.model.Channel;

public class ChannelValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Channel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		// TODO Auto-generated method stub
		Channel channel = (Channel) target;
		

		if (channel.getChannelname().isEmpty() || channel.getChannelname().length() < 5 || channel.getChannelname().length() > 25) {
			errors.rejectValue("channelname", "channelname.length");
		}
		
		if (channel.getDescription().isEmpty() || channel.getDescription().length() < 5 || channel.getDescription().length() > 150) {
			errors.rejectValue("description", "description.length");
		}

	}
}
