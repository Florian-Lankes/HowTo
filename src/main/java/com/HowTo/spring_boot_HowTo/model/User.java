package com.HowTo.spring_boot_HowTo.model;


import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.JoinColumn;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String username;
	
	@NotBlank(message = "email is mandatory")
	private String email;
	
	private boolean active = true;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate birthDate;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="userrole",
			joinColumns = @JoinColumn(name="iduser"),
			inverseJoinColumns = @JoinColumn(name="idrole")
			)	
	private List<Role> roles = new ArrayList<Role>();

	@ManyToMany(cascade = CascadeType.ALL)					//user is in groups
	private List<Group> groups = new ArrayList<Group>();
	
	@OneToMany(mappedBy = "groupowner")					//user can be the owner of many groups
	private List<Group> ownedgroups = new ArrayList<Group>();

	@NotBlank(message = "password is mandatory")
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
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
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void addGroup(Group group) {
		if(!groups.contains(group)) {
			groups.add(group);
		}
	}
	
	public void removeGroup(Group group) {
		if(groups.contains(group)) {
			groups.remove(group);
		}
	}
	
	public List<Group> getGroups(){
		return Collections.unmodifiableList(groups);
	}
	
	public void addOwnedGroup(Group group) {
		if(!ownedgroups.contains(group)) {
			ownedgroups.add(group);
		}
	}
	
	public void removeOwnedGroup(Group group) {
		if(ownedgroups.contains(group)) {
			ownedgroups.remove(group);
		}
	}
	
	public List<Group> getOwnedGroups(){
		return Collections.unmodifiableList(ownedgroups);
	}

}
