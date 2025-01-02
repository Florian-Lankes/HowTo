package com.HowTo.spring_boot_HowTo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Tutorial {
	
	private Long id;
	
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String title;
	
	@NotBlank(message = "{student.email.not.blank}")
	private String content_text;
	
	private byte[] content_video;
	
	private Long channel_id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent_text() {
		return content_text;
	}
	public void setContent_text(String content_text) {
		this.content_text = content_text;
	}
	public byte[] getContent_video() {
		return content_video;
	}
	public void setContent_video(byte[] content_video) {
		this.content_video = content_video;
	}
	public Long getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(Long channel_id) {
		this.channel_id = channel_id;
	}
}
