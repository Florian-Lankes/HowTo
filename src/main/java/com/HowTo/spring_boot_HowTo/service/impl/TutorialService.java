package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Category;
import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.repository.CategoryRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.ChannelRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.TutorialRepositoryI;

import com.HowTo.spring_boot_HowTo.service.TutorialServiceI;
@Service
public class TutorialService implements TutorialServiceI{

	@Autowired
	TutorialRepositoryI tutorialRepository;
	
	@Autowired
	ChannelRepositoryI channelRepository;
	
	@Autowired
	CategoryRepositoryI categoryRepository;
	
	@Override
	public List<Tutorial> getAllTutorials() {
		// TODO Auto-generated method stub
		return tutorialRepository.findAll();
	}

	@Override
	public Tutorial saveTutorial(Tutorial tutorial, Long channelId, Long categoryId) {
		// TODO Auto-generated method stub
		Channel channel = channelRepository.findById(channelId).get();
		Category category = categoryRepository.findById(categoryId).get();
		List<Tutorial> createdTutorials = channel.getTutorials();
		
		if(channel != null && tutorial != null && createdTutorials != null && category != null) {
			if(!createdTutorials.contains(tutorial)) {
				tutorial.setCreatedByChannel(channel);
				tutorial.setTutorialCategory(category);
				channel.addTutorial(tutorial);
				category.addTutorial(tutorial);
				tutorialRepository.save(tutorial);
			}
		}
		return tutorial;
	}

	@Override
	public Tutorial getTutorialById(Long id) {
		// TODO Auto-generated method stub
		Optional<Tutorial> opTutorial = tutorialRepository.findById(id);
		return opTutorial.isPresent()? opTutorial.get():null;
	}

	@Override
	public Tutorial updateTutorial(Tutorial tutorial, Long categoryId) {
		Category category = categoryRepository.findById(categoryId).get();
		if(tutorial != null && category != null) {
			tutorial.setTutorialCategory(category);
			category.addTutorial(tutorial);
			Tutorial local = tutorialRepository.save(tutorial);
			return local;
		}
	
		return null;
	}

	@Override
	public void delete(Tutorial tutorial) {
		// TODO Auto-generated method stub
		tutorialRepository.delete(tutorial);
	}
	
}
