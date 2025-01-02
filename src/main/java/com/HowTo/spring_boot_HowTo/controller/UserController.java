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
import com.HowTo.spring_boot_HowTo.service.UserServiceI;
import com.HowTo.spring_boot_HowTo.validator.UserValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class UserController {
	
	
	private UserServiceI userService;
	
	
	public UserController(UserServiceI userService) {
		super();
		this.userService = userService;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new UserValidator());
	}
	
	//TEST
	
	@GetMapping("/user/add")
	public String showUserAdForm(Model model, HttpServletRequest request) {
		
		User userForm = new User();
		userForm.setId((long) -1);
		LocalDate date= LocalDate.now();
		userForm.setBirthDate(date);
		
		request.getSession().setAttribute("userSession", userForm);
		model.addAttribute("user", userForm);
				
		return "users/user-add";
	}
	
    @PostMapping("/user/add")
    public String addUser(@Valid @ModelAttribute User user, 
    		BindingResult result, 
    		Model model,
    		RedirectAttributes redirectAttributes) {
    	System.out.println("In Function");
    	if (result.hasErrors()) {
    		System.out.println(result.getAllErrors().toString());
            return "/users/user-add";
        }

    	
    	userService.saveUser(user);
        redirectAttributes.addFlashAttribute("added", "User added!");
        
        return "redirect:/";
    }
	
	
	
	
	//TEST
	
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
