package com.HowTo.spring_boot_HowTo.controller;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.config.MyUserDetails;
import com.HowTo.spring_boot_HowTo.model.Comment;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.service.CommentServiceI;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/comment")
public class CommentController {

	private CommentServiceI commentService;
	
	public CommentController(CommentServiceI commentService) {
		super();
		this.commentService = commentService;
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
	
	//CREATE
		@PostMapping("/{id}/create")
		public String addComment(@PathVariable("id") Long tutorialId, @Valid @ModelAttribute Comment comment, 
				BindingResult results, Model model, 
				RedirectAttributes redirectAttributes ) {
			
			//request.getSession().setAttribute("commentSession", commentForm);
			if (results.hasErrors()) {
	    		return "redirect:/";
	        }
			
			commentService.saveComment(comment, getCurrentUserId(), tutorialId);
			redirectAttributes.addFlashAttribute("created", "Comment created!");
			return "redirect:/tutorial/view/" + tutorialId;
		}
	
}
