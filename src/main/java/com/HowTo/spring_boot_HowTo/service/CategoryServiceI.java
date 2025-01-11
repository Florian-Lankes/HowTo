package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.Category;

public interface CategoryServiceI {
	
	List<Category> getAllCategorys();
	
	Category saveCategory(Category category);
	
	Category getCategoryById(Long id);
	
	Category updateCategory(Category category);
	
	void delete(Category category);
	
}
