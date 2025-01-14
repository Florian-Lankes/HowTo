package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.Rating;

public interface RatingServiceI {
	
	List<Rating> getAllRatings();

	Rating saveRating(Rating rating, Long userid, Long tutorialid);

	Rating getRatingById(Long id);

	Rating updateRating(Rating rating);

	void delete(Rating rating);
	
}
