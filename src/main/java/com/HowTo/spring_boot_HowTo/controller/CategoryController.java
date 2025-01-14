package com.HowTo.spring_boot_HowTo.controller;

import java.time.LocalDate;
import java.util.List;

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

	public CategoryController(CategoryServiceI categoryService) {
		super();
		this.categoryService = categoryService;
	}
	
	@GetMapping("/view/{id}")
	public String getCategoryView(@PathVariable("id") Long id, Model model) {
	
		Category category = categoryService.getCategoryById(id); 
		model.addAttribute("category", category );
		List<Category> allCategoriesNotSearched = categoryService.getAllCategorys();
		allCategoriesNotSearched.remove(category);
		model.addAttribute("allCategories", allCategoriesNotSearched);

		return "categories/category";
	}

	@GetMapping("/all")
	public String showCategoryList(Model model) {
		model.addAttribute("categories", categoryService.getAllCategorys());
		return "categories/category-list";
	}

	@GetMapping("/create")
	public String showCategoryAdForm(Model model) {

		Category categoryForm = new Category();
		model.addAttribute("category", categoryForm);
		return "categories/category-create";
	}

	@PostMapping("/create")
	public String addCategory(@Valid @ModelAttribute Category category,BindingResult result, Model model ) {
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors().toString());
			return "categories/category-create";
		}
		categoryService.saveCategory(category);
		return "redirect:/category/all";
	}

	@GetMapping("/delete/{categoryId}")
	public String deleteCategory(@PathVariable("categoryId") long categoryId, Model model,
			RedirectAttributes redirectAttributes) {
		Category category = categoryService.getCategoryById(categoryId);
		categoryService.delete(category);
		redirectAttributes.addFlashAttribute("deleted", "Category deleted!");
		return "redirect:/category/all";
	}

	@GetMapping("/update/{categoryId}")
	public String showUpdateCategoryForm(@PathVariable("categoryId") Long categoryId, Model model) {
		Category category = categoryService.getCategoryById(categoryId);
		model.addAttribute("category", category);

		return "/categories/category-update";
	}

	@PostMapping("/update")
	public String updateCategory(@Valid @ModelAttribute Category category, BindingResult results, Model model,
			RedirectAttributes redirectAttributes) {

		if (results.hasErrors()) {

			return "/categories/category-update";
		}

		categoryService.updateCategory(category);
		redirectAttributes.addFlashAttribute("updated", "category updated!");
		return "redirect:/category/all";

	}
}
