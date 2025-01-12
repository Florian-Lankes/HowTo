package com.HowTo.spring_boot_HowTo.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RestController;

import com.HowTo.spring_boot_HowTo.service.UserServiceI;

@RestController
public class UserRestController {
	
	private UserServiceI userService;

	public UserRestController(UserServiceI userService) {
		super();
		this.userService = userService;
	}
}
