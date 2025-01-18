package com.HowTo.spring_boot_HowTo.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.WatchLater;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;
import com.HowTo.spring_boot_HowTo.service.WatchLaterServiceI;

@Controller
@RequestMapping("/watchLater")
public class WatchlaterController {
	
	private WatchLaterServiceI watchLaterService;
	private UserServiceI userService;
	private static final Logger logger = LogManager.getLogger(GroupController.class);
	
	
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
		Long userId = getCurrentUserId(); 
		logger.info("Fetching Watch Later list for user ID: {}", userId);
		User user = userService.getUserById(getCurrentUserId());
		List<WatchLater> watchLater = user.getWatchLater(); 
		model.addAttribute("watchLater", watchLater );
		logger.info("Watch Later list fetched successfully for user ID: {}", userId);
		return "watchLater";
		
	}
	@GetMapping("/save/{tutorialid}")
	public String saveWatchLater(@PathVariable("tutorialid") long tutorialId) {
		Long userId = getCurrentUserId(); 
		logger.info("Saving tutorial ID: {} to Watch Later list for user ID: {}", tutorialId, userId);
		if(watchLaterService.getAllWatchLaters().contains(watchLaterService.getWatchLaterById(tutorialId))) {
			logger.warn("Tutorial ID: {} already in Watch Later list for user ID: {}", tutorialId, userId);
			return ("redirect:/tutorial/view/"+ tutorialId);
		}
		
		WatchLater watchLater = new WatchLater();
		watchLater.setWatchLaterId((long) -1);
		watchLaterService.saveWatchLater(watchLater, getCurrentUserId(),tutorialId);
		logger.info("Tutorial ID: {} saved to Watch Later list for user ID: {}", tutorialId, userId);
		return ("redirect:/tutorial/view/"+ tutorialId);
		
	}
	
	@GetMapping("/delete/{id}")
    public String deleteWatchLater(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
		Long userId = getCurrentUserId(); 
		logger.info("Deleting Watch Later item ID: {} for user ID: {}", id, userId);
		WatchLater watchLater = watchLaterService.getWatchLaterById(id);               
        watchLaterService.delete(watchLater);
        logger.info("Watch Later item ID: {} deleted successfully for user ID: {}", id, userId);
        redirectAttributes.addFlashAttribute("deleted", "WatchLater deleted!");
        return "redirect:/watchLater/my";
    }
	
}
