package com.HowTo.spring_boot_HowTo.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TutorialController {
	
	
	@GetMapping("/tutorial/{tutorialid}")
	public String getTutorialId(@PathVariable String tutorialid, Model model) {
		
		System.out.println(tutorialid);
		model.addAttribute("tutorialid", tutorialid );
		return "tutorial";
	}
	
	@GetMapping("/tutorial/like")
	public String likeTutorial() {
		
		//+1 bei like
		return "tutorial";
	}
	
	@GetMapping("/tutorial/dislike")
	public String dislikeTutorial() {
		
		//-1 bei like
		return "tutorial";
	}
	
	@GetMapping("/create")
	public String createTutorial() {
		
		return "create";
	}
	
	@PostMapping("/tutorial/upload")
	public String uploadTutorial(@RequestParam String title, Model model) {
		System.out.println(title);
		ArrayList<String> tutorial = new ArrayList<>();
		
		tutorial.add(title);
		model.addAttribute("tutorial",tutorial );
		return "channel";
	}
	
}
