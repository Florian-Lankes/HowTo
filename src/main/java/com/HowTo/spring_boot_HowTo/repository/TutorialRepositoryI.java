package com.HowTo.spring_boot_HowTo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.HowTo.spring_boot_HowTo.model.Tutorial;

public interface TutorialRepositoryI extends JpaRepository<Tutorial, Long>{
	
	Page <Tutorial> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
