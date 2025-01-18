package com.HowTo.spring_boot_HowTo.controller;

import java.sql.Timestamp;

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

import com.HowTo.spring_boot_HowTo.config.MyUserDetails;
import com.HowTo.spring_boot_HowTo.model.History;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.service.HistoryServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

@Controller
@RequestMapping("/history")
public class HistoryController {
	
	private HistoryServiceI historyService;
	private UserServiceI userService;
	private static final Logger logger = LogManager.getLogger(GroupController.class);
	

	public HistoryController(HistoryServiceI historyService, UserServiceI userService) {
		super();
		this.historyService = historyService;
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
	public String getHistoryId(Model model) {
		logger.info("Entering getHistoryId method");
		User user = userService.getUserById(getCurrentUserId());
		model.addAttribute("history", user.getHistory() );
		logger.info("User history retrieved and added to model for userId: {}", getCurrentUserId());
		return "history";
	}
	
	@GetMapping("/track/{tutorialid}")
	public String trackView(@PathVariable("tutorialid") long tutorialid) {
		logger.info("Entering trackView method with tutorialId: {}", tutorialid);
		History history = new History();
		history.setHistoryId((long) -1 );
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		history.setCreationTime(currentTimestamp);
		historyService.saveHistory(history,  getCurrentUserId(),tutorialid);
		logger.info("History tracked and saved for tutorialId: {} and userId: {}", tutorialid, getCurrentUserId());
		return "redirect:/tutorial/view/{tutorialid}";
	}
	
	@GetMapping("/delete/{id}")
    public String deleteHistory(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
		logger.info("Entering deleteHistory method with historyId: {}", id);
        History history = historyService.getHistoryById(id);               
        historyService.delete(history);
        redirectAttributes.addFlashAttribute("deleted", "History deleted!");
        logger.info("History deleted successfully with historyId: {} for userId: {}", id, getCurrentUserId());
        return "redirect:/history/my";
    }
}
