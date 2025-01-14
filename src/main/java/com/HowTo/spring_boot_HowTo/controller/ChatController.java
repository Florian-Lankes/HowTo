package com.HowTo.spring_boot_HowTo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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
import com.HowTo.spring_boot_HowTo.model.MessageDTO;
import com.HowTo.spring_boot_HowTo.model.MessageDTOMapper;
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

	@Autowired
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
		User user = (User) authentication.getPrincipal();
		return user.getUserId();
	}

	@MessageMapping("/chat.sendMessage")
	// @SendTo("/topic/public")
	public void sendMessage(@Payload MessageDTO messageDTO) {
		System.out.println("Received sendMessage: " + messageDTO.getUsername());
		Message message = MessageDTOMapper.toMessage(messageDTO); // MessageDTO to Message
		messageService.saveMessage(message);
		messageTemplate.convertAndSend("/topic/group/" + message.getMessageGroup().getGroupId(), messageDTO);
	}

	@MessageMapping("/chat.UserOnline")
	public void addUser(@Payload MessageDTO messageDTO, SimpMessageHeaderAccessor headerAccessor) {
		System.out.println("Received addUser: " + messageDTO);
		Message message = MessageDTOMapper.toMessage(messageDTO);
		User user = message.getMessageOwner();
		Group group = message.getMessageGroup();
		headerAccessor.getSessionAttributes().put("user", user);
		headerAccessor.getSessionAttributes().put("group", group);
		headerAccessor.getSessionAttributes().put("messageOwner", message.getMessageOwner());
		messageService.saveMessage(message);
		messageTemplate.convertAndSend("/topic/group/" + group.getGroupId(), messageDTO);
	}

	@GetMapping("/chat/messages/{groupId}")
	@ResponseBody
	public List<MessageDTO> getOldMessages(@PathVariable Long groupId) {
		Group group = groupService.getGroupById(groupId);
		System.out.println("getOldMessages working: " + groupId);
		List<Message> messagesList = messageService.getMessagesByMessageGroup(group);
		System.out.println("Liste: " + messagesList);
		List<MessageDTO> messagesListDTO = messagesList.stream().map(MessageDTOMapper::toMessageDTO)
				.collect(Collectors.toList());
		System.out.println("Liste: " + messagesListDTO);
		return messagesListDTO;
	}

	@GetMapping("/chat/{id}")
	public String showUserRegisterForm(@PathVariable("id") Long groupId, Model model, HttpServletRequest request)
			throws JsonProcessingException {
		User user = userService.getUserById(getCurrentUserId());
		//List<Group> userGroups = user.getJoinedGroups();
		System.out.println("user: " + user);
		Group group = groupService.getGroupById(groupId);
		System.out.println("group: " + group);
		String userJson = objectMapper.writeValueAsString(user);
		String groupJson = objectMapper.writeValueAsString(group);
		//String userGroupsJson = objectMapper.writeValueAsString(userGroups);
		model.addAttribute("userJson", userJson);
		model.addAttribute("groupJson", groupJson);
		//model.addAttribute("userGroupsJson", userGroupsJson);
		return "/chat/chat";
	}

//	public void broadcastLeaveEvent(Long userId, Long groupId) {
//		MessageDTO leaveMessage = new MessageDTO();
//		//Construct new MessageDTO for leave message
//		leaveMessage.setMessageType(MessageType.LEAVE);
//		leaveMessage.setUsername(username);
//		leaveMessage.setMessageGroupId(groupId);
//		// ...
//		messagingTemplate.convertAndSend("/topic/group/" + groupId, leaveMessage);
//	}

}