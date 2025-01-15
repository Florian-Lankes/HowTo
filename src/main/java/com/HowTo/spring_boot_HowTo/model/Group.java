package com.HowTo.spring_boot_HowTo.model;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data 
@AllArgsConstructor 
@NoArgsConstructor
public class Group implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long groupId;
	
	@NotBlank(message = "Name is mandatory")
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String name;

	@NotBlank(message = "Description is mandatory")
	@Size(min = 5, max = 500, message = "{jakarta.validation.constraints.Size}")
	private String description;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate creationDate;
	
	@JsonBackReference(value = "user-joinedgroup")
	@ManyToMany(mappedBy = "joinedgroups")
	@JsonIgnore
	private List<User> users = new ArrayList<User>();
	
	
	@OneToMany(mappedBy = "messageGroup", cascade = CascadeType.REMOVE)							//user can be the owner of many comments
	@JsonManagedReference(value = "group-messages")
	private List<Message> ownedMessagesGroup = new ArrayList<Message>();
	
	@JsonBackReference(value = "user-group")
	@ManyToOne()
	private User groupOwner;
	
	// maybe needs adjustments .... private ArrayList<String> messages = new ArrayList<String>();

	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long id) {
		this.groupId = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}
	
	public void addUser(User user) {
		if(!users.contains(user)) {
			users.add(user);
		}
	}
	
	public void removeUser(User user) {
		if(users.contains(user)) {
			users.remove(user);
		}
	}
	
	public List<User> getUsers(){
		return Collections.unmodifiableList(users);
	}
	
	public void setGroupOwner(User user) {
		this.groupOwner= user;
	}
	
	public User getGroupOwner() {
		return groupOwner;
	}
}