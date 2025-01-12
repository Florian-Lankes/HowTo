package com.HowTo.spring_boot_HowTo.controller.rest;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.service.TutorialServiceI;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/tutorials")
public class TutorialRestController {

	private TutorialServiceI tutorialService;

	public TutorialRestController(TutorialServiceI tutorialService) {
		this.tutorialService = tutorialService;
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateTutorial(@PathVariable("id") Long tutorialId,
			@Valid @RequestBody Tutorial tutorial,BindingResult result, @RequestParam("categoryId") Long categoryId) {
		
		
		if(result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		
		Tutorial oldTutorial = tutorialService.getTutorialById(tutorialId);
		
		if (oldTutorial == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if(tutorial.getContentText()!=null ) {
			oldTutorial.setContentText(tutorial.getContentText());
		}
		if(tutorial.getVideoUrl() != null) {
			oldTutorial.setVideoUrl(tutorial.getVideoUrl());
		}
		if(tutorial.getTitle()!=null) {
			oldTutorial.setTitle(tutorial.getTitle());
		}
		
		Tutorial updatedTutorial = tutorialService.updateTutorial(oldTutorial, categoryId);
		EntityModel<Tutorial> entityModel = EntityModel.of(updatedTutorial);
		return ResponseEntity.ok(entityModel);

	}

	@GetMapping("/")
	public ResponseEntity<List<Tutorial>> getAllTutorials() {
		List<Tutorial> allTutorials = tutorialService.getAllTutorials();
		return ResponseEntity.ok(allTutorials);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Tutorial>> getOneTutorial(@PathVariable("id") Long tutorialId) {
		Tutorial tutorial = tutorialService.getTutorialById(tutorialId);
		if (tutorial == null) {

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		EntityModel<Tutorial> entityModel = EntityModel.of(tutorial);

		System.out.println(tutorial);
		return new ResponseEntity<>(entityModel, HttpStatus.OK);
	}

	@PostMapping(value = "/", consumes = "application/json")
	public ResponseEntity<?> postTutorial(@Valid @RequestBody Tutorial tutorial, BindingResult result,
			@RequestParam("categoryId") Long categoryId, @RequestParam("channelId") Long channelId) {
		
		if(result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors() ,HttpStatus.BAD_REQUEST);
		}
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		tutorial.setCreationTime(currentTimestamp);
		tutorial.setLikes((long) 0);
		tutorial.setDislikes((long) 0);
		Tutorial savedTutorial = tutorialService.saveTutorial(tutorial, channelId, categoryId);
		EntityModel<Tutorial> entityModel = EntityModel.of(savedTutorial);
		
		return ResponseEntity.ok(entityModel);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTutorial(@PathVariable("id") Long tutorialId){
		Tutorial tutorial = tutorialService.getTutorialById(tutorialId);
		if(tutorial==null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		tutorialService.delete(tutorial);
		return ResponseEntity.noContent().build();
	}

}
