package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Channel implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long channelId;
	
	@NotBlank(message = "Name ist notwendig")
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String channelname;
	
	@NotBlank(message = "Beschreibung ist notwendig")
	@Size(min = 5, max = 500, message = "{jakarta.validation.constraints.Size}")
	private String description;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate creationDate;
	
	@JsonManagedReference(value = "tutorial-channel")
	@OneToMany(mappedBy="createdByChannel" , cascade = CascadeType.REMOVE)
	private List<Tutorial> createdTutorials = new ArrayList<Tutorial>();
	
	@ManyToMany(mappedBy="subscribedChannels")
	private List<User> subscribedFromUserList = new ArrayList<User>();
	
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
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
	
	public void addTutorial(Tutorial tutorial) {
		if(!createdTutorials.contains(tutorial)) {
			createdTutorials.add(tutorial);
		}
	}
	
	public void removeTutorial(Tutorial tutorial) {
		if(createdTutorials.contains(tutorial)) {
			createdTutorials.remove(tutorial);
		}
	}
	
	public List<Tutorial> getTutorials(){
		return Collections.unmodifiableList(createdTutorials);
	}
	
	public void addSubscribedFromUser(User user) {
		if(!subscribedFromUserList.contains(user)) {
			subscribedFromUserList.add(user);
		}
	}
	
	public void removeSubscribedFromUser(User user) {
		if(subscribedFromUserList.contains(user)) {
			subscribedFromUserList.remove(user);
		}
	}
	
	public List<User> getSubscribedFromUserList(){
		return Collections.unmodifiableList(subscribedFromUserList);
	}
}
