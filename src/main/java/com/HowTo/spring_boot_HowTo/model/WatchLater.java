package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class WatchLater implements Serializable{

private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long watchLaterId;
	
	@ManyToOne
	private Tutorial watchLaterTutorial;
	
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
