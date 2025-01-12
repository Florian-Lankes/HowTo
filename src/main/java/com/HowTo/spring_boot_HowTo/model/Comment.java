package com.HowTo.spring_boot_HowTo.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;
	
	@NotBlank(message = "Title is mandatory")
	@Size(min = 1, max = 20, message = "{jakarta.validation.constraints.Size}")
	private String commentTitle;
	
	@NotBlank(message = "Comment can not be empty")
	@Size(min = 1, max = 500, message = "{jakarta.validation.constraints.Size}")
	private String content;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate creationDate;
	
	@JsonBackReference(value = "user-comment")
	@ManyToOne
	private User commentOwner;
	
	@JsonBackReference(value = "tutorial-comment")
	@ManyToOne
	private Tutorial commentTutorial;

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public String getCommentTitle() {
		return commentTitle;
	}

	public void setCommentTitle(String commentTitle) {
		this.commentTitle = commentTitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public User getCommentOwner() {
		return commentOwner;
	}

	public void setCommentOwner(User commentOwner) {
		this.commentOwner = commentOwner;
	}	
	public Tutorial getCommentTutorial() {
		return commentTutorial;
	}

	public void setCommentTutorial(Tutorial commentTutorial) {
		this.commentTutorial = commentTutorial;
	}
	
}
