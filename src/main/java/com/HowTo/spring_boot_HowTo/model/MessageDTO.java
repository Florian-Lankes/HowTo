package com.HowTo.spring_boot_HowTo.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MessageDTO {

	//MESSAGE
	private Long messageId;
	
	private String content;

	private MessageType messageType;
	//MESSAGE
	//private UserDTOMessage messageOwner;
	//messageOwner now directly in MessageDTO not only referenced
	//USER
	@NotBlank(message = "messageOwnerId is mandatory")
	private Long messageOwnerId;
	
	@NotBlank(message = "Name is mandatory")
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String username;

	@NotBlank(message = "email is mandatory")
	private String email;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	//USER
	//private GroupDTOMessage messageGroup;
	//messageGroup now directly in MessageDTO not only referenced
	//GROUP
	@NotBlank(message = "messageGroupId is mandatory")
	private Long messageGroupId;
	
	@NotBlank(message = "Name is mandatory")
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String groupname;

	@NotBlank(message = "Description is mandatory")
	@Size(min = 5, max = 500, message = "{jakarta.validation.constraints.Size}")
	private String description;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate creationDate;
	//GROUP
	
	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public Long getMessageOwnerId() {
		return messageOwnerId;
	}

	public void setMessageOwnerId(Long messageOwnerId) {
		this.messageOwnerId = messageOwnerId;
	}

	public Long getMessageGroupId() {
		return messageGroupId;
	}

	public void setMessageGroupId(Long messageGroupId) {
		this.messageGroupId = messageGroupId;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	
}
