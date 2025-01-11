package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Category implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;
	
	@NotBlank(message = "Name is mandatory")
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String categoryName;
	
	@OneToMany(mappedBy="tutorialCategory")
	private List<Tutorial> tutorials;
	
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void addTutorial(Tutorial tutorial) {
		if(!tutorials.contains(tutorial)) {
			tutorials.add(tutorial);
		}
	}
	
	public void removeTutorial(Tutorial tutorial) {
		if(tutorials.contains(tutorial)) {
			tutorials.remove(tutorial);
		}
	}
	
	public List<Tutorial> getTutorials(){
		return Collections.unmodifiableList(tutorials);
	}
	
	
	
}
