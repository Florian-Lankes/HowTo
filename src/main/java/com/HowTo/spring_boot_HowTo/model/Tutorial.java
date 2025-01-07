package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;

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
	
	private Timestamp creationTime;
	
	@ManyToOne
	private Channel createdByChannel;
	
	private Long likes;
	private Long dislikes;
	
	@OneToMany(mappedBy = "commentTutorial")							//Multiple Comments can be attached to one Tutorial
	private List<Comment> attachedComments = new ArrayList<Comment>();
	
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
	
	public void addAttachedComment(Comment comment){
		if(!attachedComments.contains(comment)) {
			attachedComments.add(comment);
		}
	}
	
	public void removeAttachedComment(Comment comment) {
		if(attachedComments.contains(comment)) {
			attachedComments.remove(comment);
		}
	}
	
	public List<Comment> getAttachedComments(){
		return Collections.unmodifiableList(attachedComments);
	}
	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creation_time) {
		this.creationTime = creation_time;
	}
	
	public void setCreatedByChannel(Channel channel) {
		this.createdByChannel = channel;
	}
	
	public Channel getCreatedByChannel() {
		return createdByChannel;

	}
}
