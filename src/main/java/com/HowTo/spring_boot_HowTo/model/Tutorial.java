package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@EntityListeners(AuditListener.class)
public class Tutorial extends Auditable implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tutorialId;

	@NotBlank(message = "Titel ist notwendig")
	@Size(min = 5, max = 50, message = "{jakarta.validation.constraints.Size}")
	private String title;

	private String contentText;

	private Timestamp creationTime;

	private Long likes;
	private Long dislikes;

	@JsonBackReference(value = "tutorial-channel")
	@ManyToOne
	private Channel createdByChannel;

	@JsonBackReference(value = "tutorial-category")
	@ManyToOne
	private Category tutorialCategory;

	@JsonManagedReference(value = "tutorial-watchlater")
	@OneToMany(mappedBy = "watchLaterTutorial", cascade = CascadeType.REMOVE)
	private List<WatchLater> watchLaters = new ArrayList<WatchLater>();

	@JsonManagedReference(value = "tutorial-history")
	@OneToMany(mappedBy = "historyTutorial", cascade = CascadeType.REMOVE)
	private List<History> historys = new ArrayList<History>();

	@JsonManagedReference(value = "tutorial-comment")
	@OneToMany(mappedBy = "commentTutorial", cascade = CascadeType.REMOVE) // Multiple Comments can be attached to one																// Tutorial
	private List<Comment> attachedComments = new ArrayList<Comment>();
	
	@JsonManagedReference(value="rating-tutorial")
	@OneToMany(mappedBy = "ratingTutorial", cascade = CascadeType.REMOVE)
	private List<Rating> ratings = new ArrayList<Rating>();
	
	// Cloudinary
	private String videoUrl;

	@JsonManagedReference(value = "tutorial-report")
	@OneToMany(mappedBy = "reportTutorial", cascade = CascadeType.REMOVE)
	private List<Report> reports = new ArrayList<Report>();

	public Long getTutorialId() {
		return tutorialId;
	}

	public void setTutorialId(Long tutorialId) {
		this.tutorialId = tutorialId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	public Long getLikes() {
		return likes;
	}

	public void setLikes(Long likes) {
		this.likes = likes;
	}

	public Long getDislikes() {
		return dislikes;
	}

	public void setDislikes(Long dislikes) {
		this.dislikes = dislikes;
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
	
	public void addWatchLater(WatchLater watchLater) {
		watchLaters.add(watchLater);
	}

	public void addHistory(History history) {
		historys.add(history);
	}

	public void addAttachedComment(Comment comment) {
		if (!attachedComments.contains(comment)) {
			attachedComments.add(comment);
		}
	}

	public void removeAttachedComment(Comment comment) {
		if (attachedComments.contains(comment)) {
			attachedComments.remove(comment);
		}
	}

	public List<Comment> getAttachedComments() {
		return Collections.unmodifiableList(attachedComments);
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

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creation_time) {
		this.creationTime = creation_time;
	}

	public void setCreatedByChannel(Channel channel) {
		this.createdByChannel = channel;
	}

	public Channel getCreatedByChannel() {
		return createdByChannel;

	}

	// Cloudinary
	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public Category getTutorialCategory() {
		return tutorialCategory;
	}

	public void setTutorialCategory(Category tutorialCategory) {
		this.tutorialCategory = tutorialCategory;
	}

}
