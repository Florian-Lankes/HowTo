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
@Slf4j
public class WebSocketEventListener {

	// Logger for logging important information
	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
	
	// Message template used to send messages to connected clients
	private final SimpMessageSendingOperations messageTemplate;

	// Constructor to initialize the message template
	public WebSocketEventListener(SimpMessageSendingOperations messageTemplate) { 
		this.messageTemplate = messageTemplate; 
	}
	
	// Event listener for handling WebSocket disconnection events 
	// Currently not used
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		// Wraps the event message for easier header access
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		
		// Retrieves user and group information from the session attributes
		User user = (User) headerAccessor.getSessionAttributes().get("user");
		Group group = (Group) headerAccessor.getSessionAttributes().get("group");
		
		// If a user object is found in the session, log the disconnection 
		// and send a message to all connected clients
		if (user != null) {
			logger.info("User disconnected: {}", user.getUsername());
			var message = new Message.Builder()
					.content(user.getUsername() + " is now inactive in this Chat!")
					.messageType(MessageType.LEAVE)
					.messageOwner(user)
					.messageGroup(group)
					.build();
			// Sends the disconnection message to a public topic
			messageTemplate.convertAndSend("/topic/public", message);
		}

	}
}
