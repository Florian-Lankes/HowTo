package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@EntityListeners(AuditListener.class)
public class Rating extends Auditable implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ratingId;
	
	@JsonBackReference(value="rating-user")
	@ManyToOne
	private User ratingUser;
	
	@JsonBackReference(value="rating-tutorial")
	@ManyToOne
	private Tutorial ratingTutorial;
	
	@Min(value = 1, message = "Rating muss mindestens 1 sein")
	@Max(value = 5, message = "Rating darf höchstens 5 sein")
	private int ratingScore;
	
	private String ratingText;

	public Long getRatingId() {
		return ratingId;
	}

	public void setRatingId(Long ratingId) {
		this.ratingId = ratingId;
	}

	public User getRatingUser() {
		return ratingUser;
	}

	public void setRatingUser(User ratingUser) {
		this.ratingUser = ratingUser;
	}

	public Tutorial getRatingTutorial() {
		return ratingTutorial;
	}

	public void setRatingTutorial(Tutorial ratingTutorial) {
		this.ratingTutorial = ratingTutorial;
	}

	public int getRatingScore() {
		return ratingScore;
	}

	public void setRatingScore(int ratingScore) {
		this.ratingScore = ratingScore;
	}

	public String getRatingText() {
		return ratingText;
	}

	public void setRatingText(String ratingText) {
		this.ratingText = ratingText;
	}
	
	
}
