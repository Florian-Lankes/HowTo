package com.HowTo.spring_boot_HowTo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.HowTo.spring_boot_HowTo.service.impl.MessageService;

@Controller //TODO maybe @Restcontroller
@RequestMapping("/messages")
public class MessageController {

//	@Autowired 
//	private MessageService messageService;
}
