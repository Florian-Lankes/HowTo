package com.HowTo.spring_boot_HowTo.model;

import java.sql.Timestamp;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class AuditListener {

	@PrePersist
	private void prePersist(Object object) {
		auditPersist(object);
	}

	@PreUpdate
	private void preUpdate(Object object) {
		
		auditUpdate(object);
	}

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal() instanceof String) {
			throw new IllegalStateException("User is not authenticated");
		}
		User user = (User) authentication.getPrincipal();
		return user.getUserId();
	}

	private void auditPersist(Object entity) {
		String operation = "INSERT";
		if (entity instanceof Auditable) {
			Auditable auditable = (Auditable) entity;
			Timestamp now = new Timestamp(System.currentTimeMillis());
			auditable.setLastOperation(operation);
			auditable.setLastChanged(now);
			auditable.setLastChangedBy(getCurrentUserId());
			auditable.setCreatedAt(now);
			auditable.setCreatedBy(getCurrentUserId());

			System.out.println(
					"Entity: " + auditable.getClass().getSimpleName() + " Operation: " + auditable.getLastOperation()
							+ " Creator Id: " + auditable.getCreatedBy() + " Created At: " + auditable.getLastChanged());
		}
	}

	private void auditUpdate(Object entity) {
		String operation = "UPDATE";
		if (entity instanceof Auditable) {
			Auditable auditable = (Auditable) entity;
			Timestamp lastChanged = new Timestamp(System.currentTimeMillis());
			
			auditable.setLastOperation(operation);
			auditable.setLastChanged(lastChanged);
			auditable.setLastChangedBy(getCurrentUserId());

			System.out.println(
					"Entity: " + auditable.getClass().getSimpleName() + " Operation: " + auditable.getLastOperation()
							+ " Last Changer Id: " + auditable.getLastChangedBy() + " Last Time Changed: " + auditable.getLastChanged());
		}
	}

}
