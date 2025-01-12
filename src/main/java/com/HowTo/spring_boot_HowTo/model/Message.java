package com.HowTo.spring_boot_HowTo.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@Getter
//@Setter
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId;
	
	private String content;

	private MessageType messageType;

	//private LocalDateTime timestamp;

	@ManyToOne
	@JsonBackReference
	private User messageOwner;

	@ManyToOne
	@JsonBackReference
	private Group messageGroup;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

//	public LocalDateTime getTimestamp() {
//		return timestamp;
//	}
//
//	public void setTimestamp(LocalDateTime timestamp) {
//		this.timestamp = timestamp;
//	}

	public User getMessageOwner() {
		return messageOwner;
	}

	public void setMessageOwner(User messageOwner) {
		this.messageOwner = messageOwner;
	}

	public Group getMessageGroup() {
		return messageGroup;
	}

	public void setMessageGroup(Group messageGroup) {
		this.messageGroup = messageGroup;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	
	public Message() {}

	private Message(Builder builder) {
		this.messageId = builder.messageId;
		this.content = builder.content;
		this.messageType = builder.messageType;
		//this.timestamp = builder.timestamp;
		this.messageOwner = builder.messageOwner;
		this.messageGroup = builder.messageGroup;
	}

	// Static inner Builder class
	public static class Builder {
		private Long messageId;
		private String content;
		private MessageType messageType;
		//private LocalDateTime timestamp;
		private User messageOwner;
		private Group messageGroup;

		public Builder messageId(Long messageId) {
			this.messageId = messageId;
			return this;
		}

		public Builder content(String content) {
			this.content = content;
			return this;
		}

		public Builder messageType(MessageType messageType) {
			this.messageType = messageType;
			return this;
		}

//		public Builder timestamp(LocalDateTime timestamp) {
//			this.timestamp = timestamp;
//			return this;
//		}

		public Builder messageOwner(User messageOwner) {
			this.messageOwner = messageOwner;
			return this;
		}

		public Builder messageGroup(Group messageGroup) {
			this.messageGroup = messageGroup;
			return this;
		}

		public Message build() {
			return new Message(this);
		}

	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
}
