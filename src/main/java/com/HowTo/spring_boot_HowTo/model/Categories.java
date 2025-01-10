package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Categories implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	@Id
	private Long categoryId;
	
	@NotBlank(message = "Name is mandatory")
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String categoryName;
	
	//@OneToMany
	private Tutorial categoryTutorial;
	
	
	
	
	
}
