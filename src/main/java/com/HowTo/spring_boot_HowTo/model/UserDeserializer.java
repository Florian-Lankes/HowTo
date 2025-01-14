package com.HowTo.spring_boot_HowTo.model;

import java.io.IOException;

import com.HowTo.spring_boot_HowTo.service.UserServiceI;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class UserDeserializer extends JsonDeserializer<User>{
	
	private UserServiceI userService;
	
	
	@Override
	public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		// TODO Auto-generated method stub
		String userId = p.nextTextValue();
		System.out.println("TEST");
		System.out.println(userId);
		long userId2 = Long.parseLong(userId, 10);
		return userService.getUserById(userId2);
	}

}
