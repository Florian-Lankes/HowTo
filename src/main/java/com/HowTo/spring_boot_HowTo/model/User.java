package com.HowTo.spring_boot_HowTo.model;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.JoinColumn;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "password is mandatory")
	@Size(min = 0, message = "{jakarta.validation.constraints.Size}")
	private String password;

	private boolean enabled;
	
	public User() {
		super();
		this.enabled = false;
		this.setUsingOauth(false);
		this.setSecret(Base32.random());

	}

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

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	private boolean isUsing2FA;

    private String secret;
     
    private boolean isUsingOauth;
        
    @OneToOne(mappedBy="user")
    private VerificationToken verificationToken;
    
    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "walletOwner", cascade = CascadeType.REMOVE)
    private Wallet wallet;

    @JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "userrole", joinColumns = @JoinColumn(name = "iduser"), inverseJoinColumns = @JoinColumn(name = "idrole"))
	private List<Role> roles = new ArrayList<Role>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL) // user is in groups
	private List<Group> joinedgroups = new ArrayList<Group>();

	@JsonManagedReference(value = "user-group")
	@OneToMany(mappedBy = "groupOwner", cascade = CascadeType.REMOVE) // user can be the owner of many groups
	private List<Group> ownedgroups = new ArrayList<Group>();

	@JsonManagedReference(value = "user-comment")
	@OneToMany(mappedBy = "commentOwner", cascade = CascadeType.REMOVE) // user can be the owner of many comments
	private List<Comment> ownedComments = new ArrayList<Comment>();
	// private boolean isAdmin;

	@JsonManagedReference(value = "user-history")
	@OneToMany(mappedBy = "historyOwner", cascade = CascadeType.REMOVE)
	private List<History> history = new ArrayList<History>();
	// private boolean isCreator;

	@JsonManagedReference(value = "user-report")
	@OneToMany(mappedBy = "reportUser", cascade = CascadeType.REMOVE)
	private List<Report> reports = new ArrayList<Report>();

	@JsonManagedReference(value = "user-watchlater")
	@OneToMany(mappedBy = "watchLaterOwner", cascade = CascadeType.REMOVE)
	private List<WatchLater> watchLater = new ArrayList<WatchLater>();
	
	@JsonManagedReference(value="rating-user")
	@OneToMany(mappedBy = "ratingUser", cascade = CascadeType.REMOVE)
	private List<Rating> ratings = new ArrayList<Rating>();

	@OneToMany(mappedBy = "messageOwner") // user can be the owner of many comments
	@JsonManagedReference(value = "user-messages")
	private List<Message> ownedMessagesUser = new ArrayList<Message>();
	
	@JsonIgnore
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
		if (!watchLater.contains(w)) {
			watchLater.add(w);
		}
	}

	public void removeWatchLater(WatchLater w) {
		if (watchLater.contains(w)) {
			watchLater.remove(w);
		}
	}

	public List<WatchLater> getWatchLater() {
		return new ArrayList<>(watchLater);
	}

	public void addToHistory(History h) {
		if (!history.contains(h)) {
			history.add(h);
		}
	}

	public void removeHistory(History h) {
		if (history.contains(h)) {
			history.remove(h);
		}
	}

	public List<History> getHistory() {
		return new ArrayList<>(history);
	}

	public void addJoinedGroup(Group group) {
		if (!joinedgroups.contains(group)) {
			joinedgroups.add(group);
		}
	}

	public void removeJoinedGroup(Group group) {
		if (joinedgroups.contains(group)) {
			joinedgroups.remove(group);
		}
	}

	public List<Group> getJoinedGroups() {
		return new ArrayList<>(joinedgroups);
	}

	public void addOwnedGroup(Group group) {
		if (!ownedgroups.contains(group)) {
			ownedgroups.add(group);
		}
	}

	public void removeOwnedGroup(Group group) {
		if (ownedgroups.contains(group)) {
			ownedgroups.remove(group);
		}
	}

	public List<Group> getOwnedGroups() {
		return new ArrayList<>(ownedgroups);
	}

	public void addOwnedComment(Comment comment) {
		if (!ownedComments.contains(comment)) {
			ownedComments.add(comment);
		}
	}

	public void removeOwnedComment(Comment comment) {
		if (ownedComments.contains(comment)) {
			ownedComments.remove(comment);
		}
	}

	public List<Comment> getOwnedComments() {
		return new ArrayList<>(ownedComments);
	}

	public void addSubscription(Channel channel) {
		if (!subscribedChannels.contains(channel)) {
			subscribedChannels.add(channel);
		}
	}

	public void removeSubscription(Channel channel) {
		if (subscribedChannels.contains(channel)) {
			subscribedChannels.remove(channel);
		}
	}

	public List<Channel> getSubscribedChannels() {
		return new ArrayList<>(subscribedChannels);
	}
	
	public List<Message> getOwnedMessagesUser() {
		return ownedMessagesUser;
	}

	public void addRating(Rating rating) {
		if (!ratings.contains(rating)) {
			ratings.add(rating);
		}
	}

	public void removeRating(Rating rating) {
		if (ratings.contains(rating)) {
			ratings.remove(rating);
		}
	}

	public List<Rating> getRatings() {
		return Collections.unmodifiableList(ratings);
	}

	public boolean isUsing2FA() {
		return isUsing2FA;
	}

	public void setUsing2FA(boolean isUsing2FA) {
		this.isUsing2FA = isUsing2FA;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void addReport(Report report) {
		if (!reports.contains(report)) {
			reports.add(report);
		}
	}

	public void removeReport(Report report) {
		if (reports.contains(report)) {
			reports.remove(report);
		}
	}

	public List<Report> getReports() {
		return Collections.unmodifiableList(reports);
	}
	
	public VerificationToken getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(VerificationToken verificationToken) {
		this.verificationToken = verificationToken;
	}

	public boolean isUsingOauth() {
		return isUsingOauth;
	}

	public void setUsingOauth(boolean isUsingOauth) {
		this.isUsingOauth = isUsingOauth;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
	
}

