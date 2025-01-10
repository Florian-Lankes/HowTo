package com.HowTo.spring_boot_HowTo.model;


import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
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
	private List<Group> joinedgroups = new ArrayList<Group>();
	
	@OneToMany(mappedBy = "groupOwner")						//user can be the owner of many groups
	private List<Group> ownedgroups = new ArrayList<Group>();


	@NotBlank(message = "password is mandatory")
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String password;
	

	private boolean enabled;
	
	@OneToMany(mappedBy = "commentOwner")							//user can be the owner of many comments
	private List<Comment> ownedComments = new ArrayList<Comment>();
	//private boolean isAdmin;
	
	@OneToMany(mappedBy = "historyOwner")
	private List<History> history = new ArrayList<History>();
	//private boolean isCreator;
	
	 public User() {
	        super();
	        this.enabled = false;
	    }
	@OneToMany(mappedBy = "watchLaterOwner")
	private List<WatchLater> watchLater = new ArrayList<WatchLater>();
	
	@ManyToMany
	private List<Channel> subscribedChannels = new ArrayList<Channel>();
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	
	  public boolean isEnabled() {
	        return enabled;
	    }

	    public void setEnabled(boolean enabled) {
	        this.enabled = enabled;
	    }
	

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public void addToWatchLater(WatchLater w) {
		if(!watchLater.contains(w)) {
			watchLater.add(w);
		}
	}
	public void removeWatchLater(WatchLater w) {
        if(watchLater.contains(w)) {
        	watchLater.remove(w);
        }
    }
	public List<WatchLater> getWatchLater(){
        return Collections.unmodifiableList(watchLater);
    }
	public void addToHistory(History h) {
		if(!history.contains(h)) {
			history.add(h);
		}
	}
	public void removeHistory(History h) {
        if(history.contains(h)) {
        	history.remove(h);
        }
    }
	public List<History> getHistory(){
        return Collections.unmodifiableList(history);
    }

	public void addJoinedGroup(Group group) {
		if(!joinedgroups.contains(group)) {
			joinedgroups.add(group);
		}
	}
	
	public void removeJoinedGroup(Group group) {
		if(joinedgroups.contains(group)) {
			joinedgroups.remove(group);
		}
	}
	
	public List<Group> getJoinedGroups(){
		return Collections.unmodifiableList(joinedgroups);
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
	
	public void addOwnedComment(Comment comment){
		if(!ownedComments.contains(comment)) {
			ownedComments.add(comment);
		}
	}
	
	public void removeOwnedComment(Comment comment) {
		if(ownedComments.contains(comment)) {
			ownedComments.remove(comment);
		}
	}
	
	public List<Comment> getOwnedComments(){
		return Collections.unmodifiableList(ownedComments);
	}
	
	public void addSubscription(Channel channel){
		if(!subscribedChannels.contains(channel)) {
			subscribedChannels.add(channel);
		}
	}
	
	public void removeSubscription(Channel channel) {
		if(subscribedChannels.contains(channel)) {
			subscribedChannels.remove(channel);
		}
	}
	
	public List<Channel> getSubscribedChannels(){
		return Collections.unmodifiableList(subscribedChannels);
	}


}
