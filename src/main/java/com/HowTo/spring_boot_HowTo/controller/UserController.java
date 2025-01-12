package com.HowTo.spring_boot_HowTo.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.changepassword.OnChangePasswordEvent;
import com.HowTo.spring_boot_HowTo.config.MyUserDetails;
import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.VerificationToken;
import com.HowTo.spring_boot_HowTo.registration.OnRegistrationCompleteEvent;
import com.HowTo.spring_boot_HowTo.service.ChannelServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;
import com.HowTo.spring_boot_HowTo.validator.UserAlreadyExistException;
import com.HowTo.spring_boot_HowTo.validator.UserValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class UserController {

	private UserServiceI userService;
	
	private ChannelServiceI channelService;
	
	@Autowired
    private ApplicationEventPublisher eventPublisher;

	public UserController(UserServiceI userService, ChannelServiceI channelService) {
		super();
		this.userService = userService;
		this.channelService = channelService;
	}

	@InitBinder("user")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new UserValidator());
	}

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal() instanceof String) {
			throw new IllegalStateException("User is not authenticated");
		}
		User user = (User) authentication.getPrincipal();
		return user.getUserId();
	}

	@GetMapping(value = { "/user/admin", "/user/admin/all" })
	public String showUserList(Model model, @RequestParam(required = false) String keyword,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "5") int size) {
		try {
			List<User> users = new ArrayList<User>();

			// the first page is 1 for the user, 0 for the database.
			Pageable paging = PageRequest.of(page - 1, size);
			Page<User> pageUsers;
			// getting the page from the database….
			pageUsers = userService.getAllUsers(keyword, paging);

			model.addAttribute("keyword", keyword);

			users = pageUsers.getContent();
			model.addAttribute("users", users);
			// here are the variables for the paginator in the user-all view
			model.addAttribute("entitytype", "user");
			model.addAttribute("currentPage", pageUsers.getNumber() + 1);
			model.addAttribute("totalItems", pageUsers.getTotalElements());
			model.addAttribute("totalPages", pageUsers.getTotalPages());
			model.addAttribute("pageSize", size);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}
		return "/users/user-all";
	}

	@GetMapping("/user/admin/delete/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
		User user = userService.getUserById(id);
		userService.delete(user);
		redirectAttributes.addFlashAttribute("deleted", "User deleted!");
		return "redirect:/user/admin/all";
	}

	@GetMapping("/user/admin/update/{id}")
	public String showUpdateUserForm(@PathVariable Long id, Model model, HttpServletRequest request) {
		User user = userService.getUserById(id);
		model.addAttribute("user", user);
		request.getSession().setAttribute("userSession", user);
		System.out.println("updating user id=" + id);
		return "/users/user-update";
	}

	@PostMapping("/user/admin/update")
	public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult results, Model model,
			RedirectAttributes redirectAttributes) {
		if (results.hasErrors()) {

			return "/users/user-update";
		}
		userService.updateUser(user);
		redirectAttributes.addFlashAttribute("updated", "user updated!");
		return "redirect:/user/admin/all";

	}

	@GetMapping("/user/admin/add")
	public String showUserAdForm(Model model, HttpServletRequest request) {
		User userForm = new User();
		userForm.setUserId((long) -1);
		LocalDate date = LocalDate.now();
		userForm.setBirthDate(date);
		request.getSession().setAttribute("userSession", userForm);
		model.addAttribute("user", userForm);

		return "/users/user-add";
	}

	@PostMapping("/user/admin/add")
	public String addUser(@Valid @ModelAttribute User user, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		System.out.println("In Function");

		if (result.hasErrors()) {
			System.out.println(result.getAllErrors().toString());
			return "/users/user-add";
		}

		userService.saveUser(user);
		redirectAttributes.addFlashAttribute("added", "User added!");

		return "redirect:/user/admin/all";
	}

	@GetMapping("/")
	public String showUserRegisterForm(Model model, HttpServletRequest request) {

		User userForm = new User();
		LocalDate date = LocalDate.now();
		userForm.setBirthDate(date);

		request.getSession().setAttribute("userSession", userForm);
		model.addAttribute("user", userForm);

		return "/register"; 
	}
	
	@GetMapping("/registrationConfirm")
	public String confirmRegistration
	  (WebRequest request, Model model, @RequestParam("token") String token) {
	 
	    // Locale locale = request.getLocale();
	    
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    if (verificationToken == null) {
//	        String message = messages.getMessage("auth.message.invalidToken", null, locale);
//	        model.addAttribute("message", message);
	        return "redirect:/badUser"; // ?lang="  + locale.getLanguage();
	    }
	    
	    User user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
//	        String messageValue = messages.getMessage("auth.message.expired", null, locale);
//	        model.addAttribute("message", messageValue);
	        return "redirect:/badUser"; //?lang=" + locale.getLanguage();
	    } 
	    user.setEnabled(true); 
	    userService.saveRegisteredUser(user); 
	    return "redirect:/login"; // + request.getLocale().getLanguage(); 
	}

	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute User user, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request){
		System.out.println("In Function");
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors().toString());
			return "/register";
		}
		
		try {
	        User registered = userService.saveUser(user);
	        String appUrl = request.getContextPath();
	        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, 
	          request.getLocale(), appUrl));
	    } catch (UserAlreadyExistException uaeEx) {
	    	redirectAttributes.addFlashAttribute("register", "An account for that username/email already exists.");
	        return "redirect:/";
	    }
		System.out.println(user);
		
		
		redirectAttributes.addFlashAttribute("added", "User added!");

		return "redirect:/login";
	}
	
	@GetMapping("/user/changemypassword")
	public String changePassword(Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		User user = userService.getUserById(getCurrentUserId());
		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(new OnChangePasswordEvent(user, request.getLocale(),appUrl));
		
		redirectAttributes.addFlashAttribute("email", "E-Mail for password change has been sent");
		return "redirect:/user/my";
	}
	
	@GetMapping("/confirmPassword")
	public String confirmpassword
	  (WebRequest request, Model model, @RequestParam("token") String token) {
	 
	    // Locale locale = request.getLocale();
	    
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    if (verificationToken == null) {
//	        String message = messages.getMessage("auth.message.invalidToken", null, locale);
//	        model.addAttribute("message", message);
	        return "redirect:/badUser"; // ?lang="  + locale.getLanguage();
	    }
	    
	    User user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
//	        String messageValue = messages.getMessage("auth.message.expired", null, locale);
//	        model.addAttribute("message", messageValue);
	        return "redirect:/badUser"; //?lang=" + locale.getLanguage();
	    } 
	    
	    model.addAttribute("user", user);
	    model.addAttribute("token", token);
	    return "changepassword"; // + request.getLocale().getLanguage(); 
	}
	
	@PostMapping("/user/passwordchanged")
	public String passwordchanged(@Valid @ModelAttribute User user, @RequestParam("token") String token,  BindingResult result, Model model,
	RedirectAttributes redirectAttributes) {
		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
	        return "redirect:/badUser";
	    }
		
		User u = verificationToken.getUser();
		User check = userService.getUserById(user.getUserId());
		if(check.equals(u)) {
		userService.changePassword(user);
		}
		return "redirect:/logout";
	}
	
	@GetMapping("user/my/activate2fa")
	public String activate2fa(Model model) throws UnsupportedEncodingException{
		System.out.println("In Function");
		User user = userService.getUserById(getCurrentUserId());
		String qr = userService.generateQRUrl(user);
		model.addAttribute("qr", userService.generateQRUrl(user));
		System.out.println("this is the qr code: " + qr);
		user.setUsing2FA(true);
		userService.saveRegisteredUser(user);
        return "users/qrcode";
		
	}
	
	@GetMapping("user/my/deactivate2fa")
	public String deactivate2fa(Model model){
		User user = userService.getUserById(getCurrentUserId());
		user.setUsing2FA(false);
		userService.saveRegisteredUser(user);
        return "/login";
		
	}

	@GetMapping("/login")
	public String showLoginForm() {
		
		return "login";
}
	
	@GetMapping("/logout")
	public String logout() {
		
		return "redirect:/";
}
//
//	@PostMapping("/login/process")
//	public String processLogin(@RequestParam String user, @RequestParam String password) {
//		// TODO: process POST request
//		System.out.println(user);
//		System.out.println(password);
//		return "/home";
//	}
	
	@GetMapping("/user/my")
	public String showUserProfile(Model model, RedirectAttributes redirectAttributes) {
		
		User current_user = userService.getUserById(getCurrentUserId());
		model.addAttribute("user", current_user);
		
		Channel my_channel = channelService.getChannelById(getCurrentUserId());
		model.addAttribute("channel", my_channel);
		
		// Change password
		// Change channelname and discription
		
		
		//Enable 2fa
		
		
		
		
		return "/users/profile";
	}
	

}
