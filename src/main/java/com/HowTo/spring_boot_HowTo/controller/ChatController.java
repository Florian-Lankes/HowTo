package com.HowTo.spring_boot_HowTo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.HowTo.spring_boot_HowTo.config.MyUserDetails;
import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.model.Message;
import com.HowTo.spring_boot_HowTo.model.MessageType;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.service.GroupServiceI;
import com.HowTo.spring_boot_HowTo.service.MessageServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ChatController {

	private UserServiceI userService;

	private GroupServiceI groupService;
	private final ObjectMapper objectMapper;
	private MessageServiceI messageService;
	private final SimpMessageSendingOperations messageTemplate;

	public ChatController(UserServiceI userService, GroupServiceI groupService, MessageServiceI messageService,
			ObjectMapper objectMapper, SimpMessageSendingOperations messageTemplate) {
		super();
		this.userService = userService;
		this.groupService = groupService;
		this.messageService = messageService;
		this.objectMapper = objectMapper;
		this.messageTemplate = messageTemplate;
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
	// @SendTo("/topic/public") // TODO maybe for groups is need /topic/group
	public void sendMessage(@Payload Message message) {
		System.out.println("Received sendMessage: " + message.getMessageOwner().getUsername());

		messageService.saveMessage(message);
		messageTemplate.convertAndSend("/topic/group/" + message.getMessageGroup().getGroupId(), message);
		// return message; // Send message to all subscribers of "/topic/group"
	}

	@MessageMapping("/chat.addUser")
	// @SendTo("/topic/public") // TODO maybe for groups is need /topic/group
	public void addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
		// System.out.println(headerAccessor.getNativeHeader("userId").get(0));
		// long userId = Long.parseLong(headerAccessor.getNativeHeader("userId").get(0),
		// 10);
		System.out.println("Received addUser: " + message);
		User user = message.getMessageOwner();
		Group group = message.getMessageGroup();
		headerAccessor.getSessionAttributes().put("user", user);
		headerAccessor.getSessionAttributes().put("group", group);
		// message.setMessageOwner(userService.getUserById(userId));
		// message.setMessageType(MessageType.JOIN);//
		headerAccessor.getSessionAttributes().put("messageOwner", message.getMessageOwner());

		messageService.saveMessage(message);
		messageTemplate.convertAndSend("/topic/group/" + group.getGroupId(), message);

		// return message;
	}

	@GetMapping("/chat/messages/{groupId}")
	@ResponseBody
	public List<Message> getOldMessages(@PathVariable Long groupId) {
		Group group = groupService.getGroupById(groupId);
		System.out.println("getOldMessages working:");
		return messageService.getMessagesByMessageGroup(group);
	}

	@GetMapping("/chat/{id}") // @PathVariable("id") Long groupId ,
	public String showUserRegisterForm(@PathVariable("id") Long groupId, Model model, HttpServletRequest request)
			throws JsonProcessingException {
		User user = userService.getUserById(getCurrentUserId());
		System.out.println("user: " + user);
		Group group = groupService.getGroupById(groupId);
		System.out.println("group: " + group);
		// Group group = groupService.getGroupById(groupId);

		String userJson = objectMapper.writeValueAsString(user);
		String groupJson = objectMapper.writeValueAsString(group);
		//List<Message> oldMessages = messageService.getMessagesByMessageGroup(group);
		
		model.addAttribute("userJson", userJson);
		model.addAttribute("groupJson", groupJson);
		//model.addAttribute("oldMessages", oldMessages);

		// model.addAttribute("user", user);
		// model.addAttribute("username", user.getUsername());

		// model.addAttribute("group", group);
		return "/chat/chat";
	}

}