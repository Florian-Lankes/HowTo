package com.HowTo.spring_boot_HowTo.controller;

import java.sql.Timestamp;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.HowTo.spring_boot_HowTo.config.MyUserDetails;
import com.HowTo.spring_boot_HowTo.model.History;
import com.HowTo.spring_boot_HowTo.service.HistoryServiceI;

@Controller
@RequestMapping("/history")
public class HistoryController {
	
	private HistoryServiceI historyService;
	
	public HistoryController(HistoryServiceI historyService) {
		super();
		this.historyService = historyService;
	}
	
	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal() instanceof String) {
			throw new IllegalStateException("User is not authenticated");
		}
		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
		return userDetails.getId();
	}
	
	@GetMapping("/{id}")
	public String getHistoryId(@PathVariable("id") Long id, Model model) {
		
		History history = historyService.getHistoryById(id); 
		model.addAttribute("history", history );
		return "history";
	}
	
	@GetMapping("/track/{tutorialid}")
	public String trackView(@PathVariable("tutorialid") long tutorialid) {
		History history = new History();
		history.setId((long) -1 );
		history.setUserId(getCurrentUserId());
		history.setTutorialId(tutorialid);
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		history.setTime(currentTimestamp);
		historyService.saveHistory(history);
		return "redirect:/tutorial/all";
	}
	
}
