package com.HowTo.spring_boot_HowTo.controller.rest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/tutorials", produces = MediaType.APPLICATION_JSON_VALUE)
public class TutorialRestController {

	private TutorialServiceI tutorialService;

	public TutorialRestController(TutorialServiceI tutorialService) {
		this.tutorialService = tutorialService;
	}
	
	
	@Operation(summary = "Upload a tutorial")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "201", description = "Tutorial created", 
					content = { @Content(mediaType = "application/json", 
				    schema = @Schema(implementation = Tutorial.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid Parameters", 
		    content = @Content)})
	@PostMapping(value = "/", consumes = "application/json")
	public ResponseEntity<?> postTutorial(@io.swagger.v3.oas.annotations.parameters.RequestBody(
		    description = "Tutorial to create", required = true,
		    content = @Content(mediaType = "application/json",
		      schema = @Schema(implementation = Tutorial.class),
		      examples = @ExampleObject(value = "{ \"title\": \"How to do muscle ups\" , \"contentText\": \"This is how you do a muscle up. First you have to warm up........\" }"))) 
			@Valid @RequestBody Tutorial tutorial, BindingResult result,
			@Parameter(description = "id of category") @RequestParam("categoryId") Long categoryId, @Parameter(description = "id of creator/channel") @RequestParam("channelId") Long channelId) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		tutorial.setCreationTime(currentTimestamp);
		tutorial.setLikes((long) 0);
		tutorial.setDislikes((long) 0);
		Tutorial savedTutorial = tutorialService.saveTutorial(tutorial, channelId, categoryId);
		EntityModel<Tutorial> entityModel = EntityModel.of(savedTutorial);
		Link tutorialLink = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(TutorialRestController.class).getOneTutorial(savedTutorial.getTutorialId()))
				.withSelfRel();
		entityModel.add(tutorialLink);

		return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
	}
	
	
	@Operation(summary = "Update a tutorial")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Updated the tutorial", 
					content = { @Content(mediaType = "application/json", 
				    schema = @Schema(implementation = Tutorial.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid Parameters", 
		    content = @Content), 
			@ApiResponse(responseCode = "404", description = "Tutorial not found", 
				    content = @Content) })
	@PutMapping("/{id}")
	public ResponseEntity<?> updateTutorial(@Parameter(description = "id of tutorial") @PathVariable("id") Long tutorialId, 
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				    description = "Tutorial to update", required = true,
				    content = @Content(mediaType = "application/json",
				      schema = @Schema(implementation = Tutorial.class),
				      examples = @ExampleObject(value = "{ \"title\": \"How to do 22 planches\" , \"contentText\": \"This is how you do 22 planche. First you have to warm up........\" }"))) 
			@Valid @RequestBody Tutorial tutorial,
			BindingResult result, @Parameter(description = "id of category") @RequestParam("categoryId") Long categoryId) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}

		Tutorial oldTutorial = tutorialService.getTutorialById(tutorialId);

		if (oldTutorial == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (tutorial.getContentText() != null) {
			oldTutorial.setContentText(tutorial.getContentText());
		}
		if (tutorial.getVideoUrl() != null) {
			oldTutorial.setVideoUrl(tutorial.getVideoUrl());
		}
		if (tutorial.getTitle() != null) {
			oldTutorial.setTitle(tutorial.getTitle());
		}

		Tutorial updatedTutorial = tutorialService.updateTutorial(oldTutorial, categoryId);
		EntityModel<Tutorial> entityModel = EntityModel.of(updatedTutorial);
		Link tutorialLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TutorialRestController.class)
				.getOneTutorial(updatedTutorial.getTutorialId())).withSelfRel();
		entityModel.add(tutorialLink);
		return new ResponseEntity<>(entityModel, HttpStatus.OK);

	}
	
	
	@Operation(summary = "Get all tutorials")
	@ApiResponses(value = { 
		@ApiResponse(responseCode = "200", description = "All tutorials", 
			content = { @Content(mediaType = "application/json")}),
		@ApiResponse(responseCode = "204", description = "No tutorials", content = @Content)})
	@GetMapping("/")
	public ResponseEntity<?> getAllTutorials() {
		List<Tutorial> allTutorials = tutorialService.getAllTutorials();
		if (allTutorials.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		List<EntityModel<Tutorial>> tutorialModels = new ArrayList();
		for (Tutorial tutorial : allTutorials) {
			EntityModel<Tutorial> entityModel = EntityModel.of(tutorial);
			Link tutorialLink = WebMvcLinkBuilder.linkTo(
					WebMvcLinkBuilder.methodOn(TutorialRestController.class).getOneTutorial(tutorial.getTutorialId()))
					.withSelfRel();
			entityModel.add(tutorialLink);
			tutorialModels.add(entityModel);
		}
		Link listLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(TutorialRestController.class).getAllTutorials()).withSelfRel();

		return new ResponseEntity<>(CollectionModel.of(tutorialModels, listLink), HttpStatus.OK);
	}
	
	
	@Operation(summary = "Get tutorials by title / paging filter")
	@ApiResponse(responseCode = "200", description = "Paging Infos", 
			content = { @Content(mediaType = "application/json")})
	//OpenAPI can do it
		@GetMapping("/filter")
		public Page<Tutorial> filterTutorials(@Parameter(description = "title searched for") @RequestParam("title") String title, @ParameterObject Pageable pageable) {
		     return tutorialService.getAllTutorials(title, pageable);
		}

	
	@Operation(summary = "Get a tutorial by its id")
	@ApiResponses(value = { 
	@ApiResponse(responseCode = "200", description = "Found the tutorial", 
			content = { @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = Tutorial.class)) }),
	@ApiResponse(responseCode = "404", description = "Tutorial not found", 
		    content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Tutorial>> getOneTutorial(@Parameter(description = "id of tutorial to be searched") @PathVariable("id") Long tutorialId) {
		Tutorial tutorial = tutorialService.getTutorialById(tutorialId);
		if (tutorial == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		EntityModel<Tutorial> entityModel = EntityModel.of(tutorial);
		Link tutorialLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(TutorialRestController.class).getOneTutorial(tutorialId))
				.withSelfRel();
		entityModel.add(tutorialLink);

		return new ResponseEntity<>(entityModel, HttpStatus.OK);
	}
	
	
	@Operation(summary = "Delete a tutorial by its id")
	@ApiResponses(value = { 
	@ApiResponse(responseCode = "204", description = "Deleted the tutorial", content = @Content),
	@ApiResponse(responseCode = "404", description = "Tutorial not found", content = @Content)})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTutorial(@Parameter(description = "id of tutorial to be deleted") @PathVariable("id") Long tutorialId) {
		Tutorial tutorial = tutorialService.getTutorialById(tutorialId);
		if (tutorial == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		tutorialService.delete(tutorial);
		return ResponseEntity.noContent().build();
	}

}
