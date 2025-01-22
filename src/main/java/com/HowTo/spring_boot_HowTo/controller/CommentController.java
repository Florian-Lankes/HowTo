package com.HowTo.spring_boot_HowTo.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import com.HowTo.spring_boot_HowTo.model.Comment;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.service.CommentServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/comment")
public class CommentController {

	private CommentServiceI commentService;
	private UserServiceI userService;
	private static final Logger logger = LogManager.getLogger(GroupController.class);
	
	
	public CommentController(CommentServiceI commentService, UserServiceI userService) {
		super();
		this.commentService = commentService;
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
	
	//CREATE
		@PostMapping("/{id}/create")
		public String addComment(@PathVariable("id") Long tutorialId, @Valid @ModelAttribute Comment comment, 
				BindingResult results, Model model, 
				RedirectAttributes redirectAttributes ) {
			logger.info("Entering addComment method with tutorialId: {}", tutorialId); 
			logger.debug("Comment content: {}", comment.getContent());
			//request.getSession().setAttribute("commentSession", commentForm);
			if (results.hasErrors()) {
				logger.debug("Comment Creation Error");
	    		return "redirect:/tutorial/view/" + tutorialId;
	        }
			commentService.saveComment(comment, getCurrentUserId(), tutorialId);
			redirectAttributes.addFlashAttribute("created", "Comment created!");
			logger.info("Comment created successfully for tutorialId: {}", tutorialId);
			return "redirect:/tutorial/view/" + tutorialId;
		}
		
		// updates rating done on previous site
		@PostMapping("/update")
		public String updateComment(@Valid @ModelAttribute Comment comment,
				BindingResult result) {
			logger.info("Entering updateComment method with commentId: {}", comment.getCommentId());
			if(result.hasErrors()) {
				logger.error("Validation errors: {}", result.getAllErrors());
				return "comments/comment-update";
			}
			commentService.updateComment(comment);
			Tutorial tutorialOfComment = comment.getCommentTutorial();
			logger.info("Comment updated successfully with commentId: {}", comment.getCommentId());
			return "redirect:/tutorial/view/" +  tutorialOfComment.getTutorialId();
		}
		
		
	    //delete comment
	    @GetMapping("/delete/{id}")
	    public String deleteComment(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
	    	logger.info("Entering deleteComment method with commentId: {}", id);
	        Comment comment = commentService.getCommentById(id);  
	        Tutorial tutorialOfComment = comment.getCommentTutorial();
	        if(comment.getCommentOwner().getUserId() != getCurrentUserId() && !userService.checkAdmin(userService.getUserById(getCurrentUserId()))) {
	        	//if owner deletion failed
	        	logger.warn("User with id {} is not owner of comment with id {}, deletion failed", getCurrentUserId(), id);
	   	 		redirectAttributes.addFlashAttribute("failed", "not owner!!");
	   	 		return "redirect:/tutorial/view/" +  tutorialOfComment.getTutorialId();
	   	 	}
	        commentService.delete(comment);
	        redirectAttributes.addFlashAttribute("deleted", "Comment deleted!");
	        logger.info("User with id {} deleted c with id {}", getCurrentUserId(), id);
	        return "redirect:/tutorial/view/" +  tutorialOfComment.getTutorialId();
	    }
		
}










