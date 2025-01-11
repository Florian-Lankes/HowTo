package com.HowTo.spring_boot_HowTo.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.config.MyUserDetails;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.WatchLater;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;
import com.HowTo.spring_boot_HowTo.service.WatchLaterServiceI;
import com.HowTo.spring_boot_HowTo.service.impl.UserService;

@Controller
@RequestMapping("/watchLater")
public class WatchlaterController {
	
	private WatchLaterServiceI watchLaterService;
	private UserServiceI userService;
	
	public WatchlaterController(WatchLaterServiceI watchLaterService, UserServiceI userService) {
		super();
		this.watchLaterService = watchLaterService;
		this.userService = userService;
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
	
	@GetMapping("/my")
	public String getWatchLaterId(Model model) {
		User user = userService.getUserById(getCurrentUserId());
		List<WatchLater> watchLater = user.getWatchLater(); 
		model.addAttribute("watchLater", watchLater );
		return "watchLater";
		
	}
	@GetMapping("/save/{tutorialid}")
	public String saveWatchLater(@PathVariable("tutorialid") long tutorialid) {
		WatchLater watchLater = new WatchLater();
		watchLater.setWatchLaterId((long) -1);
		watchLaterService.saveWatchLater(watchLater, getCurrentUserId(),tutorialid);
		return "redirect:/watchLater/my";
		
	}
	
	@GetMapping("/delete/{id}")
    public String deleteWatchLater(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        WatchLater watchLater = watchLaterService.getWatchLaterById(id);               
        watchLaterService.delete(watchLater);
        redirectAttributes.addFlashAttribute("deleted", "WatchLater deleted!");
        return "redirect:/watchLater/my";
    }
	
}
