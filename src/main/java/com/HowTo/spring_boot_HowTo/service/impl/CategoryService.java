package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Advertisement;
import com.HowTo.spring_boot_HowTo.model.Category;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.repository.CategoryRepositoryI;
import com.HowTo.spring_boot_HowTo.service.CategoryServiceI;
@Service
public class CategoryService implements CategoryServiceI{

	
	@Autowired
	CategoryRepositoryI	categoryRepository;
	
	@Override
	public List<Category> getAllCategorys() {
		// TODO Auto-generated method stub
		return categoryRepository.findAll();
	}

	@Override
	public Category saveCategory(Category category) {
		// TODO Auto-generated method stub
		return categoryRepository.save(category);
	}

	@Override
	public Category getCategoryById(Long id) {
		Optional<Category> opCategory = categoryRepository.findById(id);
		return opCategory.isPresent()? opCategory.get():null;
	}

	@Override
	public Category updateCategory(Category category) {
		Category local = categoryRepository.save(category);
		return local;
	}

	@Override
	public void delete(Category category) {
		List<Tutorial> tutorials = category.getTutorials();
        for (Tutorial tutorial : tutorials) {
            tutorial.setTutorialCategory(null);
            } 
        
        List<Advertisement> advertisements = category.getAdvertisements();
        for (Advertisement a : advertisements) {
            a.setAdvertisementCategory(null);
            } 
		categoryRepository.delete(category);
	}

	
}
