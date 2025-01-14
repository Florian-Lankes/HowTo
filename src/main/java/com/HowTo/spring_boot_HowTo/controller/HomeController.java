package com.HowTo.spring_boot_HowTo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.HowTo.spring_boot_HowTo.model.Category;
import com.HowTo.spring_boot_HowTo.model.Group;
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
	public String HowToView(Model model, @RequestParam(required = false) String keyword,
			@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false,
			defaultValue = "5") int size) {
		
	try {
			
			List<Tutorial> tutorials = new ArrayList<Tutorial>();

			 //the first page is 1 for the channel, 0 for the database.
			 Pageable paging = PageRequest.of(page - 1, size);
			 Page<Tutorial> pageTutorial;
			 //getting the page from the database….
			 pageTutorial = tutorialService.getAllTutorials(keyword, paging);

			 model.addAttribute("keyword", keyword);

			 tutorials = pageTutorial.getContent();
			 model.addAttribute("tutorial", tutorials);
			 //here are the variables for the paginator in the channel-all view
			 model.addAttribute("entitytype", "tutorial");
			 model.addAttribute("currentPage", pageTutorial.getNumber() + 1);
			 model.addAttribute("totalItems", pageTutorial.getTotalElements());
			 model.addAttribute("totalPages", pageTutorial.getTotalPages());
			 model.addAttribute("pageSize", size);
			 
		} catch (Exception e){
			model.addAttribute("message", e.getMessage());
		}
				
		List<Category> category = categoryService.getAllCategorys();
		model.addAttribute("category", category);
		return "home";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/content")
		public String layout() {
		return "content";
	}
	
	
}