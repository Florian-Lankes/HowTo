package com.HowTo.spring_boot_HowTo.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class MessageDTO {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId;
	
	private String content;

	private MessageType messageType;
	
}
