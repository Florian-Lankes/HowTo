package com.HowTo.spring_boot_HowTo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
//@Table(name = "`user`")
public class User implements Serializable{

	//evtl. Gender add
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Name is mandatory")
	@Size(min = 5, max = 50, message = "test")
	private String username;
	
	@NotBlank(message = "email is mandatory")
	private String email;
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate birthDate;
	
	@NotBlank(message = "password is mandatory")
	@Size(min = 5, max = 50, message = "test")
	private String password;
	
	//private boolean isAdmin;
	
	
	//private boolean isCreator;
	
	
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
//	public boolean isAdmin() {
//		return isAdmin;
//	}
//	public void setAdmin(boolean isAdmin) {
//		this.isAdmin = isAdmin;
//	}
//	public boolean isCreator() {
//		return isCreator;
//	}
//	public void setCreator(boolean isCreator) {
//		this.isCreator = isCreator;
//	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
