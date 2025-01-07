package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.Tutorial;


public interface TutorialServiceI {
	
	List<Tutorial> getAllTutorials();
	
	Tutorial saveTutorial(Tutorial tutorial, Long channelId);
	
	Tutorial getTutorialById(Long id);
	
	Tutorial updateTutorial(Tutorial tutorial);
	
	void delete(Tutorial tutorial);
}
