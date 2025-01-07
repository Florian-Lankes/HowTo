package com.HowTo.spring_boot_HowTo.repository.impl;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.WatchLater;
import com.HowTo.spring_boot_HowTo.repository.WatchLaterRepositoryI;

public interface WatchLaterRepository extends WatchLaterRepositoryI{
	List<WatchLater> findByWatchLaterOwner(User watchLaterOwner);
}
