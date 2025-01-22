package com.HowTo.spring_boot_HowTo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Logger logger = LogManager.getLogger(GroupController.class);
	

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
		logger.info("Entering sendMessage method with messageDTO: {}", messageDTO);
		Message message = MessageDTOMapper.toMessage(messageDTO); // MessageDTO to Message
		messageService.saveMessage(message);
		messageTemplate.convertAndSend("/topic/group/" + message.getMessageGroup().getGroupId(), messageDTO);
		logger.info("Message sent to group {} with messageId: {}", message.getMessageGroup().getGroupId(), message.getMessageId());
	}

	//NOT USED RIGHT NOW
	@MessageMapping("/chat.UserOnline")
	public void addUser(@Payload MessageDTO messageDTO, SimpMessageHeaderAccessor headerAccessor) {
		logger.info("Entering addUser method with messageDTO: {}", messageDTO);
		Message message = MessageDTOMapper.toMessage(messageDTO);
		User user = message.getMessageOwner();
		Group group = message.getMessageGroup();
		headerAccessor.getSessionAttributes().put("user", user);
		headerAccessor.getSessionAttributes().put("group", group);
		headerAccessor.getSessionAttributes().put("messageOwner", message.getMessageOwner());
		messageService.saveMessage(message);
		messageTemplate.convertAndSend("/topic/group/" + group.getGroupId(), messageDTO);
		logger.info("User {} added to group {} and message sent with messageId: {}", user.getUserId(), group.getGroupId(), message.getMessageId());
	}

	@GetMapping("/chat/messages/{groupId}")
	@ResponseBody
	public List<MessageDTO> getOldMessages(@PathVariable Long groupId) {
		logger.info("Entering getOldMessages method with groupId: {}", groupId);
		Group group = groupService.getGroupById(groupId);
		List<Message> messagesList = messageService.getMessagesByMessageGroup(group);
		List<MessageDTO> messagesListDTO = messagesList.stream().map(MessageDTOMapper::toMessageDTO)
				.collect(Collectors.toList());
		logger.info("Old messages retrieved for groupId: {}", groupId);
		return messagesListDTO;
	}

	@GetMapping("/chat/{id}")
	public String showUserRegisterForm(@PathVariable("id") Long groupId, Model model, HttpServletRequest request)
			throws JsonProcessingException {
		logger.info("Entering showUserRegisterForm method with groupId: {}", groupId);
		User user = userService.getUserById(getCurrentUserId());
		//List<Group> userGroups = user.getJoinedGroups();
		Group group = groupService.getGroupById(groupId);
		String userJson = objectMapper.writeValueAsString(user);
		String groupJson = objectMapper.writeValueAsString(group);
		//String userGroupsJson = objectMapper.writeValueAsString(userGroups);
		model.addAttribute("userJson", userJson);
		model.addAttribute("groupJson", groupJson);
		logger.info("User and group information added to model for groupId: {}", groupId);
		//model.addAttribute("userGroupsJson", userGroupsJson);
		return "chat/chat";
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