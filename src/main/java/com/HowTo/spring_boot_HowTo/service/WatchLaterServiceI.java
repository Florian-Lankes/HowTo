package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.WatchLater;

public interface WatchLaterServiceI {
	
	List<WatchLater> getAllWatchLaters();
	
	WatchLater saveWatchLater(WatchLater watchLater, Long userid, Long tutorialid);
	
	WatchLater getWatchLaterById(Long id);
	
	WatchLater updateWatchLater(WatchLater watchLater);
	
	void delete(WatchLater watchLater);
}
