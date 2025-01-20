package com.HowTo.spring_boot_HowTo.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.model.Rating;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.service.RatingServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/rating")
public class RatingController {
	
	private RatingServiceI ratingService;
	private UserServiceI userService;
	private static final Logger logger = LogManager.getLogger(GroupController.class);
	

	public RatingController(RatingServiceI ratingService, UserServiceI userService) {
		super();
		this.ratingService = ratingService;
		this.userService = userService;

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
	// returns all ratings (not used anymore/yet) but maybe later usefull
	@GetMapping("/all")
	public String showRatings(Model model) {
		logger.info("Entering showRatings method");
		List<Rating> AllRatings = ratingService.getAllRatings();
		model.addAttribute("ratings", AllRatings);
		logger.info("All ratings retrieved and added to model");
		return "ratings/rating-list";
	}
	// deletes a rating
	@GetMapping("/delete/{id}")
	public String deleteRating(@PathVariable("id") Long ratingId, RedirectAttributes redirectAttributes) {
		logger.info("Entering deleteRating method with ratingId: {}", ratingId);
		Rating rating = ratingService.getRatingById(ratingId);
		ratingService.delete(rating);
		redirectAttributes.addFlashAttribute("deleted", "Rating deleted!");
		logger.info("Rating deleted successfully with ratingId: {}", ratingId);
		return "redirect:/rating/myratings";
	}
	
	// shows one rating (not used anymore/yet) but maybe later usefull
	@GetMapping("/view/{id}")
	public String viewRating(@PathVariable("id") long ratingId, Model model) {
		logger.info("Entering viewRating method with ratingId: {}", ratingId);
		Rating rating = ratingService.getRatingById(ratingId);
		model.addAttribute("rating",rating);
		logger.info("Rating retrieved and added to model with ratingId: {}", ratingId);
		return "/ratings/rating";
	}
	
	//shows the create page for a rating
	@GetMapping("/tutorial/{tutorialId}")
	public String ratingTutorialView(@PathVariable("tutorialId") long tutorialId, Model model) {
		logger.info("Entering ratingTutorialView method with tutorialId: {}", tutorialId);
		Rating rating = new Rating();
		model.addAttribute("rating", rating);
		model.addAttribute("tutorialId", tutorialId);
		logger.info("Rating form created and added to model for tutorialId: {}", tutorialId);
		return "/ratings/rating-create";
	}	
	//creates the rating made on previous site
	@PostMapping("/tutorial/{tutorialId}")
	public String ratingTutorial(@PathVariable("tutorialId") Long tutorialId, @Valid @ModelAttribute Rating rating,
			BindingResult result) {
		logger.info("Entering ratingTutorial method with tutorialId: {}", tutorialId);
		if(result.hasErrors()) {
			return "ratings/rating-create";
		}
		ratingService.saveRating(rating, getCurrentUserId(), tutorialId);
		logger.info("Rating saved successfully for tutorialId: {}", tutorialId);
		return "redirect:/tutorial/view/" + tutorialId; 
	}
	// shows the update page for a rating
	@GetMapping("/update/{id}")
	public String updateRatingView(@PathVariable("id") long ratingId, Model model) {
		logger.info("Entering updateRatingView method with ratingId: {}", ratingId);
		Rating rating = ratingService.getRatingById(ratingId);
		model.addAttribute("rating", rating);
		logger.info("Rating retrieved and added to model with ratingId: {}", ratingId);
		return "/ratings/rating-update";
	}	
	// updates rating done on previous site
	@PostMapping("/update")
	public String updateRating(@Valid @ModelAttribute Rating rating,
			BindingResult result) {
		logger.info("Entering updateRating method with ratingId: {}", rating.getRatingId());
		if(result.hasErrors()) {
			logger.error("Validation errors: {}", result.getAllErrors());
			return "ratings/rating-update";
		}
		ratingService.updateRating(rating);
		logger.info("Rating updated successfully with ratingId: {}", rating.getRatingId());
		return "redirect:/rating/myratings"; 
	}
	
	// returns all the ratings the user created
		@GetMapping("/myratings")
		public String showMyRatings(Model model) {
			logger.info("Entering showMyRatings method");
			User u = userService.getUserById(getCurrentUserId());	
			List<Rating> myratings  = u.getRatings();
			model.addAttribute("ratings", myratings);
			logger.info("All my ratings retrieved and added to model");
			return "ratings/rating-list";
		}
	
}
