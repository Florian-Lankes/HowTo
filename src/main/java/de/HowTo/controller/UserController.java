package de.HowTo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.HowTo.model.User;

@Controller
public class UserController {	

	
	//Aufgabe5
	@RequestMapping(value = "/students/add", method = RequestMethod.POST)
	public String showStudentAddModelForm( Model model) {
				
		User student = new User();
		
		model.addAttribute("student", student);
				
		return "/students/studentmodel";
	}
	
	
	@PostMapping("/students/modify")
	public String showStudentAddModelForm( Model model) {
				
		User student = new User();
		
		model.addAttribute("student", student);
				
		return "/students/studentmodel";
	}
	
}

