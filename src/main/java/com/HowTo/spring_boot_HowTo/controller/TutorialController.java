package com.HowTo.spring_boot_HowTo.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.service.TutorialServiceI;
import com.HowTo.spring_boot_HowTo.validator.TutorialValidator;

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
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new TutorialValidator());
	}
	
	@GetMapping("/{id}")
	public String getTutorialId(@PathVariable("id") Long id, Model model) {
		
		Tutorial tutorial = tutorialService.getTutorialById(id); 
		model.addAttribute("tutorial", tutorial );
		return "tutorial";
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
		tutorial.setId((long) -1); //TODO change dynamically after user authorization is implemented
		tutorial.setLikes((long) 0);
		tutorial.setDislikes((long) 0);
		model.addAttribute("tutorial", tutorial);
		
		return "tutorials/tutorial-create";
	}
	
	@PostMapping("/upload")
	public String uploadTutorial(@Valid @ModelAttribute Tutorial tutorial, 
			BindingResult results, Model model, 
			RedirectAttributes redirectAttributes ) {
		
		if (results.hasErrors()) {
    		return "/tutorials/tutorial-create";
        }
		
		tutorialService.saveTutorial(tutorial);
		redirectAttributes.addFlashAttribute("created", "Tutorial created!");
		
		return "redirect:/tutorial/all";
	}
	
	@GetMapping("/delete/{id}")
    public String deleteTutorial(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        Tutorial tutorial = tutorialService.getTutorialById(id);               
        tutorialService.delete(tutorial);
        redirectAttributes.addFlashAttribute("deleted", "Tutorial deleted!");
        return "redirect:/tutorial/all";
    }
	
	@GetMapping("/update/{id}")
	public String showUpdateTutorialForm(@PathVariable("id") Long id, 
			Model model,
			HttpServletRequest request) {
	 	Tutorial tutorial = tutorialService.getTutorialById(id); 
    	model.addAttribute("tutorial", tutorial);
		
		System.out.println("updating tutorial id="+ id);
		return "/tutorials/tutorial-update";
	}
    
    
    @PostMapping("/update")
	public String updateTutorial(@Valid @ModelAttribute Tutorial tutorial,
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
