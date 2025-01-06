package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Tutorial implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tutorialId;
	
	@NotBlank(message = "Title is mandatory")
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String title;
	
	private String contentText;
	
	private byte[] contentVideo;
	
	private Long channelId;
	
	private Long likes;
	private Long dislikes;
	
	public Long getTutorialId() {
		return tutorialId;
	}
	public void setTutorialId(Long tutorialId) {
		this.tutorialId = tutorialId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContentText() {
		return contentText;
	}
	public void setContentText(String contentText) {
		this.contentText = contentText;
	}
	public byte[] getContentVideo() {
		return contentVideo;
	}
	public void setContentVideo(byte[] contentVideo) {
		this.contentVideo = contentVideo;
	}
	public Long getChannelId() {
		return channelId;
	} 
	public void setChannelId(Long channel_id) {
		this.channelId = channel_id;
	}
	public Long getLikes() {
		return likes;
	}
	public void setLikes(Long likes) {
		this.likes = likes;
	}
	public Long getDislikes() {
		return dislikes;
	}
	public void setDislikes(Long dislikes) {
		this.dislikes = dislikes;
	}
}
