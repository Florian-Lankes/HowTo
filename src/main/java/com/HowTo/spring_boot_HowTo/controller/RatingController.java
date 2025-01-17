package com.HowTo.spring_boot_HowTo.controller;

import java.util.List;

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
	@GetMapping("/all")
	public String showRatings(Model model) {

		List<Rating> AllRatings = ratingService.getAllRatings();
		model.addAttribute("ratings", AllRatings);

		return "ratings/rating-list";
	}
	@GetMapping("/delete/{id}")
	public String deleteRating(@PathVariable("id") Long ratingId, RedirectAttributes redirectAttributes) {
		Rating rating = ratingService.getRatingById(ratingId);
		ratingService.delete(rating);
		redirectAttributes.addFlashAttribute("deleted", "Rating deleted!");
		return "redirect:/rating/all";
	}
	@GetMapping("/view/{id}")
	public String viewRating(@PathVariable("id") long ratingId, Model model) {
		Rating rating = ratingService.getRatingById(ratingId);
		model.addAttribute("rating",rating);
		return "/ratings/rating";
	}
	
	@GetMapping("/tutorial/{tutorialId}")
	public String ratingTutorialView(@PathVariable("tutorialId") long tutorialId, Model model) {
		Rating rating = new Rating();
		model.addAttribute("rating", rating);
		model.addAttribute("tutorialId", tutorialId);
		
		return "/ratings/rating-create";
	}	
	@PostMapping("/tutorial/{tutorialId}")
	public String ratingTutorial(@PathVariable("tutorialId") Long tutorialId, @Valid @ModelAttribute Rating rating,
			BindingResult result) {
		if(result.hasErrors()) {
			return "ratings/rating-create";
		}
		ratingService.saveRating(rating, getCurrentUserId(), tutorialId);
		return "redirect:/rating/view/" + rating.getRatingId(); 
	}
	
	@GetMapping("/update/{id}")
	public String updateRatingView(@PathVariable("id") long ratingId, Model model) {
		Rating rating = ratingService.getRatingById(ratingId);
		model.addAttribute("rating", rating);

		
		return "/ratings/rating-update";
	}	
	@PostMapping("/update")
	public String updateRating(@Valid @ModelAttribute Rating rating,
			BindingResult result) {
		
		if(result.hasErrors()) {
			return "ratings/rating-update";
		}
		ratingService.updateRating(rating);
		return "redirect:/rating/view/" + rating.getRatingId(); 
	}
	
}
