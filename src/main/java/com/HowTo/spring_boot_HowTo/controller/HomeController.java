package com.HowTo.spring_boot_HowTo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.HowTo.spring_boot_HowTo.model.Category;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.service.CategoryServiceI;
import com.HowTo.spring_boot_HowTo.service.TutorialServiceI;

@Controller
public class HomeController {
	
	private TutorialServiceI tutorialService;
	private CategoryServiceI categoryService;
	
	public HomeController(TutorialServiceI tutorialService, CategoryServiceI categoryService) {
		super();
		this.tutorialService = tutorialService;
		this.categoryService = categoryService;
	}
	
	@GetMapping("/home")
	public String HowToView(Model model) {
		
		List<Tutorial> tutorial = tutorialService.getAllTutorials();
		List<Category> category = categoryService.getAllCategorys();
		model.addAttribute("tutorial", tutorial);
		model.addAttribute("category", category);
		return "home";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/content")
		public String layout() {
		return "content";
	}
	
	
}