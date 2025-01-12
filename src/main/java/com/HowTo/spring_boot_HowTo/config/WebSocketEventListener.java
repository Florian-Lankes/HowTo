package com.HowTo.spring_boot_HowTo.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.model.Message;
import com.HowTo.spring_boot_HowTo.model.MessageType;
import com.HowTo.spring_boot_HowTo.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	private final SimpMessageSendingOperations messageTemplate = null;

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		User user = (User) headerAccessor.getSessionAttributes().get("user");
		Group group = (Group) headerAccessor.getSessionAttributes().get("group");
		System.out.println("IN EVENT LISTENER");
		if (user != null) {
			logger.info("User disconnected: {}", user.getUsername());
			var message = new Message.Builder()
					.content("Hello, World!")
					.messageType(MessageType.LEAVE)
					.messageOwner(user)
					.messageGroup(group)
					.build();
			messageTemplate.convertAndSend("/topic/public", message);
		}

	}
}
