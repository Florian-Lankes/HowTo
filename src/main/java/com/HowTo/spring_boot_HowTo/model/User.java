package com.HowTo.spring_boot_HowTo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class User {

	private Long id;
	
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String username;
	
	@NotBlank(message = "{student.email.not.blank}")
	private String email;
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate birthDate;
	
	
	private boolean isAdmin;
	
	
	private boolean isCreator;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public boolean isCreator() {
		return isCreator;
	}
	public void setCreator(boolean isCreator) {
		this.isCreator = isCreator;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
}
