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

import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/api/ratings")
public class RatingRestController {

	private RatingServiceI ratingService;
	
	public RatingRestController(RatingServiceI ratingService) {
		this.ratingService = ratingService;
	}
	
	
	@PostMapping(value = "/", consumes = "application/json")
	public ResponseEntity<?> postRating(@Valid @RequestBody Rating rating, BindingResult result, 
			@RequestParam("userId") Long userId, @RequestParam("tutorialId") Long tutorialId) {

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
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateRating(@PathVariable("id") Long ratingId, @Valid @RequestBody Rating rating,
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
	
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Rating>> getOneRating(@PathVariable("id") Long ratingId) {
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
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteRating(@PathVariable("id") Long ratingId) {
		Rating rating = ratingService.getRatingById(ratingId);
		if (rating == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		ratingService.delete(rating);
		return ResponseEntity.noContent().build();
	}
}
