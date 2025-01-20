package com.HowTo.spring_boot_HowTo.config;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	// Registers STOMP endpoints, mapping each to a specific URL
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// Adds an endpoint that clients will use to connect to the WebSocket server 
		// The withSockJS() method enables SockJS fallback options for browsers that don’t support WebSocket
		registry.addEndpoint("/ws").withSockJS();
	}

	// Configures message broker options
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// Sets the prefix for messages that are bound for methods annotated with @MessageMapping
		registry.setApplicationDestinationPrefixes("/app");
		// Enables a simple in-memory message broker and configures one or more prefixes to filter destinations handled by the broker
		registry.enableSimpleBroker("/topic");
	}

	// Configures the message converters to be used
	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		// Adds a custom message converter to handle JSON message conversion
		messageConverters.add(jackson2MessageConverter());
		return true;
	}
	// Defines a bean for MappingJackson2MessageConverter which converts messages to JSON and vice versa
	@Bean
	public MappingJackson2MessageConverter jackson2MessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		// Sets a custom ObjectMapper for the converter
		converter.setObjectMapper(objectMapper());
		return converter;
	}

	// Defines a bean for ObjectMapper which will handle JSON serialization and deserialization
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		// Registers a module to support Java 8 date and time API
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}
}
