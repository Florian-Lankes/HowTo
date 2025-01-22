package com.HowTo.spring_boot_HowTo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

	@GetMapping("/admin")
	public String HowToAdminView(Model model) {
		
		
		return "admin/admin";
	}
	
	@GetMapping("/admin/reports")
	public String HowToReportsView(Model model) {
		
		
		return "admin/reports";
	}
	
}
