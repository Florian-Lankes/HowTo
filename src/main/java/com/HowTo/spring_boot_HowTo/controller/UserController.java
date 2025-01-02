package com.HowTo.spring_boot_HowTo.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.validator.UserValidator;

import jakarta.validation.Valid;

@Controller
public class UserController {
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new UserValidator());
	}
	
	
	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}
	
	@PostMapping("/login/process")
	public String processLogin(@RequestParam String user, @RequestParam String password) {
		//TODO: process POST request
		System.out.println(user);
		System.out.println(password);
		return "/home";
	}
	
	
	@RequestMapping(value = "/registerUser")
	public String showUserForm(Model model) {
		
		User user = new User();
		user.setId((long) -1);
		
		LocalDate date= LocalDate.now();
		
		
		user.setBirthDate(date);
		System.out.println(date+"********");
		model.addAttribute("user", user);
		
	return "/users/register-user";
		
	}
	
	
	@RequestMapping(value = "/registerUser/process")
	public String addStudent(@ModelAttribute @Valid User userRequest, 
			BindingResult result, 
			RedirectAttributes attr){
					
		System.out.println(userRequest.getUsername());
		System.out.println(userRequest.getBirthDate());
		
		if (result.hasErrors()) {
			System.out.println(result.getErrorCount());
			System.out.println(result.getAllErrors());
			return "/users/register-user";
		
		}
		
		//userService.save(user);
		attr.addFlashAttribute("success", "User added!");
		return "redirect:/login";
	}
	
	
		
}
