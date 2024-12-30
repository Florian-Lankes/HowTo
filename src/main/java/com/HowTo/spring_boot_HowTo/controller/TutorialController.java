package com.HowTo.spring_boot_HowTo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TutorialController {
	
	@GetMapping("/tutorial")
	public String HowToTutorialView() {
		
		return "tutorial";
	}
}
