package com.HowTo.spring_boot_HowTo.controller;


import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.List;
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

import com.HowTo.spring_boot_HowTo.config.MyUserDetails;

import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.service.TutorialServiceI;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/tutorial")
public class TutorialController {
	
	private TutorialServiceI tutorialService;
	
	public TutorialController(TutorialServiceI tutorialService) {
		super();
		this.tutorialService = tutorialService;
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
	
	@GetMapping("/view/{id}")
	public String getTutorialView(@PathVariable("id") Long id, Model model) {
		
		Tutorial tutorial = tutorialService.getTutorialById(id); 
		
		//For comment Form in Tutorial
		Comment commentForm = new Comment();
		commentForm.setCommentId((long) -1); //TODO change dynamically after user authorization is implemented
		LocalDate date= LocalDate.now();
		commentForm.setCreationDate(date);
		
		model.addAttribute("tutorial", tutorial );
		model.addAttribute("comment", commentForm);
		//model.addAttribute("allCommentsOfTutorial", tutorial.getAttachedComments());
		return "tutorials/tutorial";
	}
	
	@GetMapping("/like/{id}")
	public String likeTutorial(@PathVariable("id") Long id, 
			Model model,
			HttpServletRequest request) {
	 	Tutorial tutorial = tutorialService.getTutorialById(id); 
	 	tutorial.setLikes(tutorial.getLikes()+1);
		tutorialService.updateTutorial(tutorial);

		return "redirect:/tutorial/all";
	}
	
	@GetMapping("/dislike/{id}")
	public String dislikeTutorial(@PathVariable("id") Long id, 
			Model model,
			HttpServletRequest request) {
	 	Tutorial tutorial = tutorialService.getTutorialById(id); 
	 	tutorial.setDislikes(tutorial.getDislikes()+1);
		tutorialService.updateTutorial(tutorial);
		
		return "redirect:/tutorial/all";
	}
	
	@GetMapping("/all")
	public String showChannelList(Model model) {
		
    	List<Tutorial> AllTutorials = tutorialService.getAllTutorials();
		model.addAttribute("tutorials", AllTutorials);
				
		return "tutorials/tutorial-list";
	}
	
	@GetMapping("/create")
	public String createTutorialView(Model model, HttpServletRequest request) {
		
		Tutorial tutorial = new Tutorial();
		tutorial.setTutorialId((long) -1); //TODO change dynamically after user authorization is implemented
		tutorial.setLikes((long) 0);
		tutorial.setDislikes((long) 0);
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		tutorial.setCreationTime(currentTimestamp);
		model.addAttribute("tutorial", tutorial);
		
		return "tutorials/tutorial-create";
	}
	
	@PostMapping("/upload")
	public String uploadTutorial(@Valid @ModelAttribute Tutorial tutorial, 
			BindingResult results, Model model, 
			RedirectAttributes redirectAttributes ) {
		if (results.hasErrors()) {
			System.out.println(results.getAllErrors().toString());
    		return "/tutorials/tutorial-create";
        }
		tutorialService.saveTutorial(tutorial, getCurrentUserId());
		redirectAttributes.addFlashAttribute("created", "Tutorial created!");
		
		return "redirect:/tutorial/all";
	}
	
	@GetMapping("/delete/{id}")
    public String deleteTutorial(@PathVariable("id") Long tutorialId, Model model, RedirectAttributes redirectAttributes) {
        Tutorial tutorial = tutorialService.getTutorialById(tutorialId);               
        tutorialService.delete(tutorial);
        redirectAttributes.addFlashAttribute("deleted", "Tutorial deleted!");
        return "redirect:/tutorial/all";
    }
	
	@GetMapping("/update/{id}")
	public String showUpdateTutorialForm(@PathVariable("id") Long tutorialId, 
			Model model,
			HttpServletRequest request) {
	 	Tutorial tutorial = tutorialService.getTutorialById(tutorialId); 
    	model.addAttribute("tutorial", tutorial);
    	//model.addAttribute("createdByChannel", tutorial.getCreatedByChannel());
		
		System.out.println("updating tutorial id="+ tutorialId);
		return "/tutorials/tutorial-update";
	}
    
    
    @PostMapping("/update")
	public String updateTutorial(@Valid @ModelAttribute Tutorial tutorial, //@Valid @ModelAttribute Long channel,
			BindingResult results,
			Model model, 
			RedirectAttributes redirectAttributes) {
		
		if (results.hasErrors()){
			return "/tutorials/tutorial-update";
		}
		
		tutorialService.updateTutorial(tutorial);
        redirectAttributes.addFlashAttribute("updated", "tutorial updated!");
		return "redirect:/tutorial/all";
	}
	
}
