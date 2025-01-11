package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.Advertisement;

public interface AdvertisementServiceI {
	
	List<Advertisement> getAllAdvertisements();
	
	Advertisement saveAdvertisement(Advertisement Advertisement, Long categoryId);
	
	Advertisement getAdvertisementById(Long id);
	
	Advertisement updateAdvertisement(Advertisement Advertisement , Long categoryId);
	
	void delete(Advertisement Advertisement);
	
}
