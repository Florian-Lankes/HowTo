package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.WatchLater;
import com.HowTo.spring_boot_HowTo.repository.TutorialRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.WatchLaterRepositoryI;
import com.HowTo.spring_boot_HowTo.service.WatchLaterServiceI;

@Service
public class WatchLaterService implements WatchLaterServiceI{
	
	@Autowired
	WatchLaterRepositoryI watchLaterRepository;
	
	@Autowired
	UserRepositoryI userRepository;
	
	@Autowired
	TutorialRepositoryI tutorialRepository;
	
	@Override
	public List<WatchLater> getAllWatchLaters() {
		// TODO Auto-generated method stub
		return watchLaterRepository.findAll();
	}

	@Override
	public WatchLater saveWatchLater(WatchLater watchLater, Long userid, Long tutorialid) {
        User user = userRepository.findById(userid).get();
        Tutorial tutorial = tutorialRepository.findById(tutorialid).get();
        List<WatchLater> watchLaterList = user.getWatchLater();
        if(user != null && watchLater != null && watchLaterList != null && tutorial != null) {
            if(!watchLaterList.contains(watchLater)) {
            	watchLater.setWatchLaterOwner(user);
            	watchLater.setWatchLaterTutorial(tutorial);
            	tutorial.addWatchLater(watchLater);
                user.addToWatchLater(watchLater);
                watchLaterRepository.save(watchLater);
            }
        }
		return watchLater;
	}

	@Override
	public WatchLater getWatchLaterById(Long id) {
		// TODO Auto-generated method stub
		Optional<WatchLater> opWatchLater = watchLaterRepository.findById(id);
		return opWatchLater.isPresent()? opWatchLater.get():null;
	}

	@Override
	public WatchLater updateWatchLater(WatchLater watchLater) {
		WatchLater local = watchLaterRepository.save(watchLater);
		return local;
	}

	@Override
	public void delete(WatchLater watchLater) {
		// TODO Auto-generated method stub
		watchLaterRepository.delete(watchLater);
	}

}
