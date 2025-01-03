package com.HowTo.spring_boot_HowTo.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.service.TutorialServiceI;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/tutorial")
public class TutorialController {
	
	private TutorialServiceI tutorialService;
	
	public TutorialController(TutorialServiceI tutorialService) {
		super();
		this.tutorialService = tutorialService;
	}
	
	@GetMapping("/{tutorialid}")
	public String getTutorialId(@PathVariable String tutorialid, Model model) {
		
		System.out.println(tutorialid);
		model.addAttribute("tutorialid", tutorialid );
		return "tutorial";
	}
	
	@GetMapping("/like")
	public String likeTutorial() {
		
		//+1 bei like
		return "tutorial";
	}
	
	@GetMapping("/dislike")
	public String dislikeTutorial() {
		
		//-1 bei like
		return "tutorial";
	}
	
	@GetMapping("/create")
	public String createTutorialView(Model model, HttpServletRequest request) {
		
		Tutorial tutorial = new Tutorial();
		tutorial.setId((long) -1); //TODO change dynamically after user authorization is implemented
		tutorial.setLikes((long) 0);
		tutorial.setDislikes((long) 0);
		
		model.addAttribute("tutorial", tutorial);
		
		return "tutorials/create";
	}
	
	@PostMapping("/upload")
	public String uploadTutorial(@ModelAttribute Tutorial tutorial, 
			BindingResult result, Model model, 
			RedirectAttributes redirectAttributes ) {
		tutorialService.saveTutorial(tutorial);
		redirectAttributes.addFlashAttribute("created", "Tutorial created!");
		
		return "redirect:channel";
	}
	
}
