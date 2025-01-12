package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class WatchLater implements Serializable{

private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long watchLaterId;
	
	@JsonBackReference(value = "tutorial-watchlater")
	@ManyToOne
	private Tutorial watchLaterTutorial;
	
	@JsonBackReference(value = "user-watchlater")
	@ManyToOne
	private User watchLaterOwner;
	
	public Long getWatchLaterId() {
		return watchLaterId;
	}

	public void setWatchLaterId(Long watchLaterId) {
		this.watchLaterId = watchLaterId;
	}

	public User getWatchLaterOwner() {
		return watchLaterOwner;
	}

	public void setWatchLaterOwner(User watchLaterOwner) {
		this.watchLaterOwner = watchLaterOwner;
	}

	public Tutorial getWatchLaterTutorial() {
		return watchLaterTutorial;
	}

	public void setWatchLaterTutorial(Tutorial watchLaterTutorial) {
		this.watchLaterTutorial = watchLaterTutorial;
	}
	
}
