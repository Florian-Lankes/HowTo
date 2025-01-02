package com.HowTo.spring_boot_HowTo.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

//	@GetMapping("/home")
//	public String HowToHomeView() {
//		
//		return "home";
//	}
	
	
	//@RequestMapping(method = RequestMethod.GET, value = "/")
	@GetMapping("/home")
	public String HowToHomeView(Model model) {
		
		ArrayList<String> tutorial = new ArrayList<>();
		
		tutorial.add("1");
		tutorial.add("2");
		tutorial.add("3");
		tutorial.add("4");
		tutorial.add("5");
		tutorial.add("6");
		
		model.addAttribute("tutorial", tutorial);
		
		return "home";
	}
	
	@GetMapping("/")
	public String HowToView(Model model) {
		
		ArrayList<String> tutorial = new ArrayList<>();
		
		tutorial.add("1");
		tutorial.add("2");
		tutorial.add("3");
		tutorial.add("4");
		tutorial.add("5");
		tutorial.add("6");
		
		model.addAttribute("tutorial", tutorial);
		
		return "home";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/content")
	public String layout() {
	return "content";
	}
	
	
}
