package com.HowTo.spring_boot_HowTo.controller;

import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.model.Category;
import com.HowTo.spring_boot_HowTo.model.Comment;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.service.CategoryServiceI;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/category")
public class CategoryController {
	private CategoryServiceI categoryService;
	private static final Logger logger = LogManager.getLogger(GroupController.class);
	
	public CategoryController(CategoryServiceI categoryService) {
		super();
		this.categoryService = categoryService;
	}
	
	//Returns Site with all Tutorials of the category, ensures that the selected category is the first category shown on html
	@GetMapping("/view/{id}")
	public String getCategoryView(@PathVariable("id") Long id, Model model) {
		logger.info("Entering getCategoryView method with categoryId: {}", id);
		Category category = categoryService.getCategoryById(id); 
		model.addAttribute("category", category );
		
		//remove selected category, so that they are shown behind it in html
		List<Category> allCategoriesNotSearched = categoryService.getAllCategorys();
		allCategoriesNotSearched.remove(category);
		model.addAttribute("allCategories", allCategoriesNotSearched);

		logger.info("Category retrieved and added to model with categoryId: {}", id); 
		logger.debug("All categories excluding current: {}", allCategoriesNotSearched);
		return "categories/category";
	}

	//Returns category-list for Admin
	@GetMapping("/all")
	public String showCategoryList(Model model) {
		model.addAttribute("categories", categoryService.getAllCategorys());
		return "categories/category-list";
	}
	
	//shows create page for category
	@GetMapping("/create")
	public String showCategoryAdForm(Model model) {
		logger.info("Entering showCategoryList method");
		Category categoryForm = new Category();
		model.addAttribute("category", categoryForm);
		logger.info("All categories retrieved and added to model");
		return "categories/category-create";
	}
	// creates category
	@PostMapping("/create")
	public String addCategory(@Valid @ModelAttribute Category category,BindingResult result, Model model ) {
		logger.info("Entering addCategory method with category: {}", category);
		if (result.hasErrors()) {
			logger.error("Validation errors: {}", result.getAllErrors());
			return "categories/category-create";
		}
		categoryService.saveCategory(category);
		logger.info("Category saved successfully with id: {}", category.getCategoryId());
		return "redirect:/category/all";
	}
	// deletes category 
	@GetMapping("/delete/{categoryId}")
	public String deleteCategory(@PathVariable("categoryId") long categoryId, Model model,
			RedirectAttributes redirectAttributes) {
		logger.info("Entering deleteCategory method with categoryId: {}", categoryId);
		Category category = categoryService.getCategoryById(categoryId);
		categoryService.delete(category);
		redirectAttributes.addFlashAttribute("deleted", "Category deleted!");
		logger.info("Category deleted successfully with categoryId: {}", categoryId);
		return "redirect:/category/all";
	}
	// shows update page for category
	@GetMapping("/update/{categoryId}")
	public String showUpdateCategoryForm(@PathVariable("categoryId") Long categoryId, Model model) {
		logger.info("Entering showUpdateCategoryForm method with categoryId: {}", categoryId);
		Category category = categoryService.getCategoryById(categoryId);
		model.addAttribute("category", category);
		logger.info("Category retrieved and added to model with categoryId: {}", categoryId);
		return "categories/category-update";
	}
	// updates category
	@PostMapping("/update")
	public String updateCategory(@Valid @ModelAttribute Category category, BindingResult results, Model model,
			RedirectAttributes redirectAttributes) {
		logger.info("Entering updateCategory method with categoryId: {}", category.getCategoryId());
		if (results.hasErrors()) {
			logger.error("Validation errors: {}", results.getAllErrors());
			return "categories/category-update";
		}

		categoryService.updateCategory(category);
		redirectAttributes.addFlashAttribute("updated", "category updated!");
		logger.info("Category updated successfully with categoryId: {}", category.getCategoryId());
		return "redirect:/category/all";

	}
}
