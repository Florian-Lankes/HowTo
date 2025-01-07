package com.HowTo.spring_boot_HowTo.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;
	
	@NotBlank(message = "Titel is mandatory")
	@Size(min = 1, max = 20, message = "{jakarta.validation.constraints.Size}")
	private String titel;
	
	@NotBlank(message = "Comment can not be empty")
	@Size(min = 1, max = 500, message = "{jakarta.validation.constraints.Size}")
	private String content;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate creationDate;
	
	@ManyToOne
	private User CommentOwner;	
	
}
