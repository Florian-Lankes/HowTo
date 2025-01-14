package com.HowTo.spring_boot_HowTo.controller;


import java.time.LocalDate;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.model.Advertisement;
import com.HowTo.spring_boot_HowTo.model.Category;
import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.model.Comment;
import com.HowTo.spring_boot_HowTo.config.MyUserDetails;

import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.service.CloudinaryServiceI;
import com.HowTo.spring_boot_HowTo.service.CategoryServiceI;
import com.HowTo.spring_boot_HowTo.service.ChannelServiceI;
import com.HowTo.spring_boot_HowTo.service.TutorialServiceI;
import com.HowTo.spring_boot_HowTo.subscribemsg.OnInformSubscriberEvent;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/tutorial")
public class TutorialController {
	
	@Autowired
    private ApplicationEventPublisher eventPublisher;
	
	private TutorialServiceI tutorialService;
	private CloudinaryServiceI cloudinaryService;
	private CategoryServiceI categoryService;
	private ChannelServiceI channelService;
	
	public TutorialController(TutorialServiceI tutorialService, CategoryServiceI categoryService, CloudinaryServiceI cloudinaryService, ChannelServiceI channelService) {
		super();
		this.tutorialService = tutorialService;
		this.categoryService = categoryService;
		this.cloudinaryService = cloudinaryService;
		this.channelService = channelService;

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
	
	
	@GetMapping("/view/{id}")
	public String getTutorialView(@PathVariable("id") Long id, Model model) {
		
		Tutorial tutorial = tutorialService.getTutorialById(id); 
		
		//For comment Form in Tutorial
		Comment commentForm = new Comment();
		commentForm.setCommentId((long) -1); //TODO change dynamically after user authorization is implemented
		LocalDate date= LocalDate.now();
		commentForm.setCreationDate(date);
		Category category = tutorial.getTutorialCategory();
		
		if(category != null) {
			List<Advertisement> advertisements = category.getAdvertisements();
			int size = advertisements.size();
			if (size > 0) {
				Random r = new Random();
				int random = r.nextInt(size);
			    Advertisement randomAd = advertisements.get(random);
			    String advertisement = randomAd.getVideoUrl();
			    model.addAttribute("advertisement", advertisement );
			    // Use randomAd as needed
			} else {
			    // Handle case when the list is empty
			    System.out.println("No advertisements available.");
			}
		}
		else {
			System.out.println("No category available.");
		}
		model.addAttribute("tutorial", tutorial );
		model.addAttribute("comment", commentForm);
		return "tutorials/tutorial";
	}
	
	@GetMapping("/like/{id}")
	public String likeTutorial(@PathVariable("id") Long id, 
			Model model,
			HttpServletRequest request) {
	 	Tutorial tutorial = tutorialService.getTutorialById(id); 
	 	tutorial.setLikes(tutorial.getLikes()+1);
		tutorialService.updateTutorial(tutorial, tutorial.getTutorialCategory().getCategoryId());

		return "redirect:/tutorial/all";
	}
	
	@GetMapping("/dislike/{id}")
	public String dislikeTutorial(@PathVariable("id") Long id, 
			Model model,
			HttpServletRequest request) {
	 	Tutorial tutorial = tutorialService.getTutorialById(id); 
	 	tutorial.setDislikes(tutorial.getDislikes()+1);
		tutorialService.updateTutorial(tutorial, tutorial.getTutorialCategory().getCategoryId());
		
		return "redirect:/tutorial/all";
	}
	
	@GetMapping("/all")
	public String showTutorialList(Model model, @RequestParam(required = false) String keyword,
			@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false,
			defaultValue = "5") int size) {
try {
			
			List<Tutorial> tutorials = new ArrayList<Tutorial>();

			 //the first page is 1 for the channel, 0 for the database.
			 Pageable paging = PageRequest.of(page - 1, size);
			 Page<Tutorial> pageTutorial;
			 //getting the page from the database….
			 pageTutorial = tutorialService.getAllTutorials(keyword, paging);

			 model.addAttribute("keyword", keyword);

			 tutorials = pageTutorial.getContent();
			 model.addAttribute("tutorials", tutorials);
			 //here are the variables for the paginator in the channel-all view
			 model.addAttribute("entitytype", "tutorial");
			 model.addAttribute("currentPage", pageTutorial.getNumber() + 1);
			 model.addAttribute("totalItems", pageTutorial.getTotalElements());
			 model.addAttribute("totalPages", pageTutorial.getTotalPages());
			 model.addAttribute("pageSize", size);
			 
		} catch (Exception e){
			model.addAttribute("message", e.getMessage());
		}
				
		return "tutorials/tutorial-list";
	}
	
	@GetMapping("/create")
	public String createTutorialView(Model model) {
		
		Tutorial tutorial = new Tutorial();
		tutorial.setLikes((long) 0);
		tutorial.setDislikes((long) 0);
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		tutorial.setCreationTime(currentTimestamp);
		List<Category> categories = categoryService.getAllCategorys();
		model.addAttribute("tutorial", tutorial);
		model.addAttribute("categories",categories);
		
		return "tutorials/tutorial-create";
	}
	// 
	@PostMapping("/upload")
	public String uploadTutorial(@RequestParam("categorySelection") Long categoryId, @Valid @ModelAttribute Tutorial tutorial, 
			BindingResult results, Model model, 
			RedirectAttributes redirectAttributes ) {
		if (results.hasErrors()) {
			System.out.println(results.getAllErrors().toString());
			List<Category> categories = categoryService.getAllCategorys();
			model.addAttribute("categories",categories);
    		return "/tutorials/tutorial-create";
        }
		tutorialService.saveTutorial(tutorial, getCurrentUserId(), categoryId);
		Channel c = channelService.getChannelById(getCurrentUserId());
		List<User> subscribedUsers = c.getSubscribedFromUserList();
		
		for (User u : subscribedUsers) {
			eventPublisher.publishEvent(new OnInformSubscriberEvent(u, c.getChannelname(), tutorial.getTitle()));
		}
		
		redirectAttributes.addFlashAttribute("created", "Tutorial created!");
		
		return "redirect:/tutorial/all";
	}
	
	@GetMapping("/delete/{id}")
    public String deleteTutorial(@PathVariable("id") Long tutorialId, Model model, RedirectAttributes redirectAttributes) {
        Tutorial tutorial = tutorialService.getTutorialById(tutorialId);     
        if(tutorial != null &&tutorial.getVideoUrl() != null && !tutorial.getVideoUrl().isEmpty()) {
    		
    		String s = tutorial.getVideoUrl();  //String split to get public id and delete it
    		String[] news = s.split("/");
    		String name = news[news.length-1];
    		String[] test = name.split("\\.");
    		String publicId = test[0];
    		cloudinaryService.deleteFile(publicId);
    		cloudinaryService.deleteVideoUrl(tutorialId);
    	}
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
    	List<Category> categories = categoryService.getAllCategorys();
		model.addAttribute("categories",categories);

		System.out.println("updating tutorial id="+ tutorialId);
		return "/tutorials/tutorial-update";
	}
    
    
    @PostMapping("/update")
	public String updateTutorial(@Valid @ModelAttribute Tutorial tutorial, @RequestParam("categorySelection") Long categoryId,//@Valid @ModelAttribute Long channel,
			BindingResult results,
			Model model, 
			RedirectAttributes redirectAttributes) {
		
		if (results.hasErrors()){
			return "/tutorials/tutorial-update";
		}
		
		tutorialService.updateTutorial(tutorial, categoryId);
        redirectAttributes.addFlashAttribute("updated", "tutorial updated!");
		return "redirect:/tutorial/all";
	}
    
    @PostMapping("/uploadvideo/{id}")
	public String uploadVideo(@PathVariable("id") Long tutorialId, @RequestParam("video") MultipartFile file, RedirectAttributes redirectAttributes) {
    	
    	Tutorial tutorial = tutorialService.getTutorialById(tutorialId);
    	if(tutorial.getVideoUrl() != null && !tutorial.getVideoUrl().isEmpty()) {
    		
    		String s = tutorial.getVideoUrl();  //String split to get public id and delete it
    		String[] news = s.split("/");
    		String name = news[news.length-1];
    		String[] test = name.split("\\.");
    		String publicId = test[0];
    		cloudinaryService.deleteFile(publicId);
    	}
		cloudinaryService.uploadFile(file, tutorialId);
        redirectAttributes.addFlashAttribute("updated", "tutorial video updated!");
		return "redirect:/tutorial/all";
	}
    
    @GetMapping("/deletevideo/{id}")
	public String deleteVideo(@PathVariable("id") Long tutorialId, 
			Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
    	Tutorial tutorial = tutorialService.getTutorialById(tutorialId);
    	if(tutorial.getVideoUrl() != null && !tutorial.getVideoUrl().isEmpty()) {
    		
    		String s = tutorial.getVideoUrl();  //String split to get public id and delete it
    		String[] news = s.split("/");
    		String name = news[news.length-1];
    		String[] test = name.split("\\.");
    		String publicId = test[0];
    		cloudinaryService.deleteFile(publicId);
    		cloudinaryService.deleteVideoUrl(tutorialId);
    	}
    	redirectAttributes.addFlashAttribute("deleted", "tutorial video deleted!");
		return "redirect:/tutorial/all";
	}
    
}
