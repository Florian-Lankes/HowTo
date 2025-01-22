package com.HowTo.spring_boot_HowTo.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Report implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reportId;
	
	@JsonBackReference(value = "user-report")
	@ManyToOne
	private User reportUser;
	
	@JsonBackReference(value = "tutorial-report")
	@ManyToOne
	private Tutorial reportTutorial;
	
	@NotBlank(message = "Grund ist notwendig")
	@Size(min = 5, max = 100, message = "{jakarta.validation.constraints.Size}")
	private String reportText;

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public User getReportUser() {
		return reportUser;
	}

	public void setReportUser(User reportUser) {
		this.reportUser = reportUser;
	}

	public Tutorial getReportTutorial() {
		return reportTutorial;
	}

	public void setReportTutorial(Tutorial reportTutorial) {
		this.reportTutorial = reportTutorial;
	}

	public String getReportText() {
		return reportText;
	}

	public void setReportText(String reportText) {
		this.reportText = reportText;
	}
	
	
}
