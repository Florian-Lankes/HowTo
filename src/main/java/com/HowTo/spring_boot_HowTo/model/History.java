package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class History implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long historyId;

	@JsonBackReference(value = "tutorial-history")
	@ManyToOne
	private Tutorial historyTutorial;
	
	@JsonBackReference(value = "user-history")
	@ManyToOne
	private User historyOwner;

	private Timestamp creationTime;

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	public Tutorial getHistoryTutorial() {
		return historyTutorial;
	}

	public void setHistoryTutorial(Tutorial historyTutorial) {
		this.historyTutorial = historyTutorial;
	}

	public User getHistoryOwner() {
		return historyOwner;
	}

	public void setHistoryOwner(User historyOwner) {
		this.historyOwner = historyOwner;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creation_time) {
		this.creationTime = creation_time;
	}

}
