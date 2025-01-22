package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Advertisement;
import com.HowTo.spring_boot_HowTo.model.Category;
import com.HowTo.spring_boot_HowTo.repository.AdvertisementRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.CategoryRepositoryI;
import com.HowTo.spring_boot_HowTo.service.AdvertisementServiceI;
@Service
public class AdvertisementService implements AdvertisementServiceI{

	
	@Autowired
	AdvertisementRepositoryI	AdvertisementRepository;
	
	@Autowired
	CategoryRepositoryI categoryRepository;
	
	@Override
	public List<Advertisement> getAllAdvertisements() {
		// TODO Auto-generated method stub
		return AdvertisementRepository.findAll();
	}
	
	//saves ads with their respective category
	@Override
	public Advertisement saveAdvertisement(Advertisement a, Long categoryId) {
		// TODO Auto-generated method stub
		Category category = categoryRepository.findById(categoryId).get();
		if(a != null && category != null) {
			a.setAdvertisementCategory(category);
			category.addAdvertisements(a);
			Advertisement local = AdvertisementRepository.save(a);
			return local;
		}
	
		return null;
	}

	@Override
	public Advertisement getAdvertisementById(Long id) {
		Optional<Advertisement> opAdvertisement = AdvertisementRepository.findById(id);
		return opAdvertisement.isPresent()? opAdvertisement.get():null;
	}
	
	//update Ad and maybe their category
	@Override
	public Advertisement updateAdvertisement(Advertisement a, Long categoryId) {
		
		Category category = categoryRepository.findById(categoryId).get();
		if(a != null && category != null) {
			a.setAdvertisementCategory(category);
			category.addAdvertisements(a);
			Advertisement local = AdvertisementRepository.save(a);
			return local;
		}
	
		return null;
	}

	@Override
	public void delete(Advertisement Advertisement) {
		AdvertisementRepository.delete(Advertisement);
	}

	
}
