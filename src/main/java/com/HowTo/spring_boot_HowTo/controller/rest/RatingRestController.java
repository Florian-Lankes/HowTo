package com.HowTo.spring_boot_HowTo.controller.rest;
import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

import com.HowTo.spring_boot_HowTo.model.Rating;
import com.HowTo.spring_boot_HowTo.service.RatingServiceI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/api/ratings")
public class RatingRestController {

	private RatingServiceI ratingService;
	
	public RatingRestController(RatingServiceI ratingService) {
		this.ratingService = ratingService;
	}
	
	@Operation(summary = "Create a rating")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "201", description = "Rating created", 
					content = { @Content(mediaType = "application/json", 
				    schema = @Schema(implementation = Rating.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid Parameters", 
		    content = @Content)})
	@PostMapping(value = "/", consumes = "application/json")
	public ResponseEntity<?> postRating(@io.swagger.v3.oas.annotations.parameters.RequestBody(
		    description = "Rating to create", required = true,
		    content = @Content(mediaType = "application/json",
		      schema = @Schema(implementation = Rating.class),
		      examples = @ExampleObject(value = "{ \"ratingScore\": 5,  \"ratingText\": \"This tutorial is really well made! I can do a pullup now!\" }"))) 
			@Valid @RequestBody Rating rating, BindingResult result, 
			@Parameter(description = "id of writer/reader") @RequestParam("userId") Long userId, @Parameter(description = "id of tutorial rated") @RequestParam("tutorialId") Long tutorialId) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		Rating savedRating = ratingService.saveRating(rating, userId, tutorialId);
		EntityModel<Rating> entityModel = EntityModel.of(savedRating);
		Link ratingLink = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RatingRestController.class).getOneRating(savedRating.getRatingId()))
				.withSelfRel();
		entityModel.add(ratingLink);

		return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
	}
	
	@Operation(summary = "Update a rating")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Updated the rating", 
					content = { @Content(mediaType = "application/json", 
				    schema = @Schema(implementation = Rating.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid Parameters", 
		    content = @Content), 
			@ApiResponse(responseCode = "404", description = "Rating not found", 
				    content = @Content) })
	@PutMapping("/{id}")
	public ResponseEntity<?> updateRating(@Parameter(description = "id of rating") @PathVariable("id") Long ratingId,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				    description = "Rating to update", required = true,
				    content = @Content(mediaType = "application/json",
				      schema = @Schema(implementation = Rating.class),
				      examples = @ExampleObject(value = "{ \"ratingScore\": 1,  \"ratingText\": \"This tutorial is really bad! It doesn't work at all!\" }"))) 
			@Valid @RequestBody Rating rating,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}

		Rating oldRating = ratingService.getRatingById(ratingId);

		if (oldRating == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if ((Integer) rating.getRatingScore() != null) {
			oldRating.setRatingScore(rating.getRatingScore());
		}
		if (rating.getRatingText() != null) {
			oldRating.setRatingText(rating.getRatingText());
		}


		Rating updatedRating = ratingService.updateRating(oldRating);
		EntityModel<Rating> entityModel = EntityModel.of(updatedRating);
		Link ratingLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RatingRestController.class)
				.getOneRating(updatedRating.getRatingId())).withSelfRel();
		entityModel.add(ratingLink);
		return new ResponseEntity<>(entityModel, HttpStatus.OK);

	}

	
	@Operation(summary = "Get all ratings")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "All ratings", 
				content = { @Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "204", description = "No ratings", content = @Content)})
	@GetMapping("/")
	public ResponseEntity<?> getAllRatings() {
		List<Rating> allRatings = ratingService.getAllRatings();
		if (allRatings.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		List<EntityModel<Rating>> ratingModels = new ArrayList();
		for (Rating rating : allRatings) {
			EntityModel<Rating> entityModel = EntityModel.of(rating);
			Link ratingLink = WebMvcLinkBuilder.linkTo(
					WebMvcLinkBuilder.methodOn(RatingRestController.class).getOneRating(rating.getRatingId()))
					.withSelfRel();
			entityModel.add(ratingLink);
			ratingModels.add(entityModel);
		}
		Link listLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(RatingRestController.class).getAllRatings()).withSelfRel();

		return new ResponseEntity<>(CollectionModel.of(ratingModels, listLink), HttpStatus.OK);
	}
	
	
	@Operation(summary = "Get a rating by its id")
	@ApiResponses(value = { 
	@ApiResponse(responseCode = "200", description = "Found the rating", 
			content = { @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = Rating.class)) }),
	@ApiResponse(responseCode = "404", description = "Rating not found", 
		    content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Rating>> getOneRating(@Parameter(description = "id of rating to be searched") @PathVariable("id") Long ratingId) {
		Rating rating = ratingService.getRatingById(ratingId);
		if (rating == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		EntityModel<Rating> entityModel = EntityModel.of(rating);
		Link ratingLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(RatingRestController.class).getOneRating(ratingId))
				.withSelfRel();
		entityModel.add(ratingLink);

		return new ResponseEntity<>(entityModel, HttpStatus.OK);
	}
	
	
	@Operation(summary = "Delete a rating by its id")
	@ApiResponses(value = { 
	@ApiResponse(responseCode = "204", description = "Deleted the rating", content = @Content),
	@ApiResponse(responseCode = "404", description = "Rating not found", content = @Content)})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteRating(@Parameter(description = "id of rating to be deleted") @PathVariable("id") Long ratingId) {
		Rating rating = ratingService.getRatingById(ratingId);
		if (rating == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		ratingService.delete(rating);
		return ResponseEntity.noContent().build();
	}
}
