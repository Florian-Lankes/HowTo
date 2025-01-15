package com.HowTo.spring_boot_HowTo.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Auditable {
	@Column(name = "lastOperation")
	private String lastOperation;
	
	@Column(name = "lastChanged")
	private Timestamp lastChanged;
	
	@Column(name = "lastChangedBy")
	private Long lastChangedBy;
	
	@Column(name= "createdAt")
	private Timestamp createdAt;
	
	@Column(name= "createdBy")
	private Long createdBy;

	public String getLastOperation() {
		return lastOperation;
	}

	public void setLastOperation(String lastOperation) {
		this.lastOperation = lastOperation;
	}

	public Timestamp getLastChanged() {
		return lastChanged;
	}

	public void setLastChanged(Timestamp lastChanged) {
		this.lastChanged = lastChanged;
	}

	public Long getLastChangedBy() {
		return lastChangedBy;
	}

	public void setLastChangedBy(Long lastChangedBy) {
		this.lastChangedBy = lastChangedBy;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}


	

	
}
