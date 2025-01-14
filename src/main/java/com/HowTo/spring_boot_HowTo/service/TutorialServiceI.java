package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.HowTo.spring_boot_HowTo.model.Tutorial;


public interface TutorialServiceI {
	
	List<Tutorial> getAllTutorials();
	
	Page<Tutorial> getAllTutorials(String title, Pageable pageable);
	
	Tutorial saveTutorial(Tutorial tutorial, Long channelId, Long categoryId);
	
	Tutorial getTutorialById(Long id);
	
	Tutorial updateTutorial(Tutorial tutorial, Long categoryId);
	
	void delete(Tutorial tutorial);
}
