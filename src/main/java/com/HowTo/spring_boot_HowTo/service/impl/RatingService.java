package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Rating;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.repository.RatingRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.TutorialRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;
import com.HowTo.spring_boot_HowTo.service.RatingServiceI;

@Service
public class RatingService implements RatingServiceI {

	@Autowired
	RatingRepositoryI ratingRepository;

	@Autowired
	UserRepositoryI userRepository;

	@Autowired
	TutorialRepositoryI tutorialRepository;

	@Override
	public List<Rating> getAllRatings() {
		// TODO Auto-generated method stub
		return ratingRepository.findAll();
	}

	// saves rating using userid and tutorialid because they have a @ManyToOne Relation
	// checks if user and tutorial exists and set their list, then save the rating
	@Override
	public Rating saveRating(Rating rating, Long userid, Long tutorialid) {
		User user = userRepository.findById(userid).get();
		Tutorial tutorial = tutorialRepository.findById(tutorialid).get();
		List<Rating> ratingList = user.getRatings();
		if (user != null && rating != null && ratingList != null && tutorial != null) {
			if (!ratingList.contains(rating)) {
				rating.setRatingUser(user);
				rating.setRatingTutorial(tutorial);
				tutorial.addRating(rating);
				user.addRating(rating);
				ratingRepository.save(rating);
			}
		}
		return rating;

	}

	@Override
	public Rating getRatingById(Long id) {
		// TODO Auto-generated method stub
		Optional<Rating> opRating = ratingRepository.findById(id);
		return opRating.isPresent() ? opRating.get() : null;
	}

	@Override
	public Rating updateRating(Rating rating) {
		Rating local = ratingRepository.save(rating);
		return local;
	}

	@Override
	public void delete(Rating rating) {
		// TODO Auto-generated method stub
		ratingRepository.delete(rating);
	}

}
