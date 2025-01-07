package com.HowTo.spring_boot_HowTo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.WatchLater;

public interface WatchLaterRepositoryI extends JpaRepository<WatchLater, Long>{
	
	List<WatchLater> findByWatchLaterOwner(User watchLaterOwner);
}
