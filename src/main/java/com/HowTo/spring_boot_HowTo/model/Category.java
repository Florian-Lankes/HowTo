package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@EntityListeners(AuditListener.class)
public class Category extends Auditable implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;
	
	@NotBlank(message = "Name ist notwendig")
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String categoryName;
	
	@JsonManagedReference(value = "tutorial-category")
	@OneToMany(mappedBy="tutorialCategory")
	private List<Tutorial> tutorials;
	
	@JsonManagedReference(value = "advertisement-category")
	@OneToMany(mappedBy="advertisementCategory")
	private List<Advertisement> advertisements ;
	
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
	
	public void addAdvertisements(Advertisement a) {
		if(!advertisements.contains(a)) {
			advertisements.add(a);
		}
	}
	
	public void removeAdvertisements(Advertisement a) {
		if(advertisements.contains(a)) {
			advertisements.remove(a);
		}
	}
	
	public List<Advertisement> getAdvertisements(){
		return Collections.unmodifiableList(advertisements);
	}
	
}
