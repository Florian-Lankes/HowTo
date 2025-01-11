package com.HowTo.spring_boot_HowTo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.HowTo.spring_boot_HowTo.config.MyUserDetails;
import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.model.Message;
import com.HowTo.spring_boot_HowTo.model.MessageType;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.service.GroupServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ChatController {
	
	private UserServiceI userService;
	
	private GroupServiceI groupService;
	
	public ChatController(UserServiceI userService, GroupServiceI groupService) {
        super();
        this.userService = userService;
        this.groupService = groupService;
    }
	
	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal() instanceof String) {
			throw new IllegalStateException("User is not authenticated");
		}
		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
		return userDetails.getId();
	}
	
	@MessageMapping("/chat.sendMessage") 
	@SendTo("/topic/public") // TODO maybe for groups is need /topic/group
	public Message sendMessage(@Payload Message message) { 
		return message;  // Send message to all subscribers of "/topic/group" 
	} 
	
	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public") // TODO maybe for groups is need /topic/group
	public Message addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
		//System.out.println(headerAccessor.getNativeHeader("userId").get(0));
		//long userId = Long.parseLong(headerAccessor.getNativeHeader("userId").get(0), 10);
		
		//message.setMessageOwner(userService.getUserById(userId));
		//message.setMessageType(MessageType.JOIN);//
		
		User userIdString = message.getMessageOwner();
		System.out.println(userIdString);
		return message; 
	}
	
	@GetMapping("/chat") // @PathVariable("id") Long groupId ,
	public String showUserRegisterForm(Model model, HttpServletRequest request) {
		User user = userService.getUserById(getCurrentUserId());
		System.out.println(user);
		//Group group = groupService.getGroupById(groupId);
		model.addAttribute("user", user);
		//model.addAttribute("username", user.getUsername());
		
		//model.addAttribute("groupId", groupId);
		return "/chat/chat"; 
	}
	
}