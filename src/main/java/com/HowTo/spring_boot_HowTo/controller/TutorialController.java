package com.HowTo.spring_boot_HowTo.controller;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.HowTo.spring_boot_HowTo.model.Rating;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.service.CategoryServiceI;
import com.HowTo.spring_boot_HowTo.service.ChannelServiceI;
import com.HowTo.spring_boot_HowTo.service.CloudinaryServiceI;
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
	private static final Logger logger = LogManager.getLogger(GroupController.class);
	
	
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
	
	//shows tutorial page with all comments
	@GetMapping("/view/{id}")
	public String getTutorialView(@PathVariable("id") Long id, Model model) {
		logger.info("Entering getTutorialView method with tutorialId: {}", id);
		Tutorial tutorial = tutorialService.getTutorialById(id); 
		
		//For comment Form in Tutorial
		Comment commentForm = new Comment();
		commentForm.setCommentId((long) -1); 
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
			    logger.info("Random advertisement added to model: {}", advertisement);
			    // Use randomAd as needed
			} else {
			    // Handle case when the list is empty
				logger.warn("No advertisements available.");
			}
		}
		else {
			logger.warn("No category available.");
		}
		
		
		model.addAttribute( "avgScore", calculateAvgRating(tutorial));
		model.addAttribute("tutorial", tutorial );
		model.addAttribute("comment", commentForm);
		logger.info("Tutorial and comment form added to model");
		return "tutorials/tutorial";
	}
	//calculates the average rating from a tutorial and returns the correct string to add in html
	public String calculateAvgRating(Tutorial l) {
		int counter = 0;
		double sum = 0;
		for(Rating e :  l.getRatings()) {
			counter++;
			sum += e.getRatingScore();
		}
		double avgScore ;
		String avgScoreString;
		if(sum!=0) {
			avgScore = sum/counter;
			avgScoreString = avgScore + "/5";
		}
		else {
			avgScoreString = "Noch keine vorhanden";
		}
		return avgScoreString;
	}
	
	@GetMapping("/ratings/{id}")
	public String getRatingsFromTutorial(@PathVariable("id") Long tutorialId, Model model) {
		Tutorial tutorial = tutorialService.getTutorialById(tutorialId); 
		
		model.addAttribute( "avgScore", calculateAvgRating(tutorial));
		model.addAttribute("tutorial", tutorial);
		return "tutorials/tutorial-ratings";
		
	}
	
	
	//likes a tutorial
	@GetMapping("/like/{id}")
	public String likeTutorial(@PathVariable("id") Long id, 
			Model model,
			HttpServletRequest request) {
		logger.info("Entering likeTutorial method with tutorialId: {}", id);
	 	Tutorial tutorial = tutorialService.getTutorialById(id); 
	 	tutorial.setLikes(tutorial.getLikes()+1);
		tutorialService.updateTutorial(tutorial, tutorial.getTutorialCategory().getCategoryId());
		logger.info("Tutorial liked successfully, new like count: {}", tutorial.getLikes());
		return "redirect:/tutorial/all";
	}
	
	//dislikes a tutorial
	@GetMapping("/dislike/{id}")
	public String dislikeTutorial(@PathVariable("id") Long id, 
			Model model,
			HttpServletRequest request) {
		logger.info("Entering dislikeTutorial method with tutorialId: {}", id);
	 	Tutorial tutorial = tutorialService.getTutorialById(id); 
	 	tutorial.setDislikes(tutorial.getDislikes()+1);
		tutorialService.updateTutorial(tutorial, tutorial.getTutorialCategory().getCategoryId());
		logger.info("Tutorial disliked successfully, new dislike count: {}", tutorial.getDislikes());
		return "redirect:/tutorial/all";
	}
	
	//shows all tutorials pagination
	@GetMapping("/all")
	public String showTutorialList(Model model, @RequestParam(required = false) String keyword,
			@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false,
			defaultValue = "5") int size) {
		logger.info("Entering showTutorialList method with keyword: {}, page: {}, size: {}", keyword, page, size);
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
			 logger.info("Tutorials retrieved and added to model");
		} catch (Exception e){
			logger.error("Exception occurred while retrieving tutorials: {}", e.getMessage());
			model.addAttribute("message", e.getMessage());
		}
				
		return "tutorials/tutorial-list";
	}
	
	//shows the tutorial form 
	@GetMapping("/create")
	public String createTutorialView(Model model) {
		logger.info("Entering createTutorialView method");
		Tutorial tutorial = new Tutorial();
		tutorial.setLikes((long) 0);
		tutorial.setDislikes((long) 0);
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		tutorial.setCreationTime(currentTimestamp);
		List<Category> categories = categoryService.getAllCategorys();
		model.addAttribute("tutorial", tutorial);
		model.addAttribute("categories",categories);
		
		logger.info("Tutorial creation form initialized and added to model");
		return "tutorials/tutorial-create";
	}
	
	// uploads a tutorial and publishes a mail to all subscriber
	@PostMapping("/upload")
	public String uploadTutorial(@RequestParam("categorySelection") Long categoryId, @Valid @ModelAttribute Tutorial tutorial, 
			BindingResult results, Model model, 
			RedirectAttributes redirectAttributes ) {
		logger.info("Entering uploadTutorial method with tutorial: {}", tutorial);
		if (results.hasErrors()) {
			logger.error("Validation errors: {}", results.getAllErrors());
			List<Category> categories = categoryService.getAllCategorys();
			model.addAttribute("categories",categories);
    		return "tutorials/tutorial-create";
        }
		tutorialService.saveTutorial(tutorial, getCurrentUserId(), categoryId);
		Channel c = channelService.getChannelById(getCurrentUserId());
		List<User> subscribedUsers = c.getSubscribedFromUserList();
		
		//this event sends a mail to all subscriber
		for (User u : subscribedUsers) {
			eventPublisher.publishEvent(new OnInformSubscriberEvent(u, c.getChannelname(), tutorial.getTitle()));
		}
		logger.info("Tutorial created successfully with id: {}", tutorial.getTutorialId());
		redirectAttributes.addFlashAttribute("created", "Tutorial created!");
		return "redirect:/tutorial/upload/video/"+ tutorial.getTutorialId();
	}
	
	//upload video form
	@GetMapping("/upload/video/{id}")
	public String uploadVideoView(@PathVariable("id") Long id, Model model) {
		logger.info("Entering uploadVideoView method with tutorialId: {}", id);
		model.addAttribute(tutorialService.getTutorialById(id));
		return "tutorials/tutorial-video-upload";
	}
	
	//deletes tutorial
	@GetMapping("/delete/{id}")
    public String deleteTutorial(@PathVariable("id") Long tutorialId, Model model, RedirectAttributes redirectAttributes) {
		logger.info("Entering deleteTutorial method with tutorialId: {}", tutorialId);
		Tutorial tutorial = tutorialService.getTutorialById(tutorialId);     
        if(tutorial != null &&tutorial.getVideoUrl() != null && !tutorial.getVideoUrl().isEmpty()) {
        	//deletes video if it existed on cloudinary
    		String s = tutorial.getVideoUrl();  //String split to get public id and delete it
    		String[] news = s.split("/");
    		String name = news[news.length-1];
    		String[] test = name.split("\\.");
    		String publicId = test[0];
    		cloudinaryService.deleteFile(publicId);
    		cloudinaryService.deleteVideoUrl(tutorialId);
    		logger.info("Tutorial video deleted with publicId: {}", publicId);
    	}
        tutorialService.delete(tutorial);
        redirectAttributes.addFlashAttribute("deleted", "Tutorial deleted!");
        logger.info("Tutorial deleted successfully with tutorialId: {}", tutorialId);
        return "redirect:/tutorial/all";
    }
	
	//update tutorial form
	@GetMapping("/update/{id}")
	public String showUpdateTutorialForm(@PathVariable("id") Long tutorialId, 
			Model model,
			HttpServletRequest request) {
		logger.info("Entering showUpdateTutorialForm method with tutorialId: {}", tutorialId);
	 	Tutorial tutorial = tutorialService.getTutorialById(tutorialId); 
    	model.addAttribute("tutorial", tutorial);
    	List<Category> categories = categoryService.getAllCategorys();
		model.addAttribute("categories",categories);
		logger.info("Tutorial retrieved and added to model for update with tutorialId: {}", tutorialId);
		return "tutorials/tutorial-update";
	}
    
    //update tutorial
    @PostMapping("/update")
	public String updateTutorial(@Valid @ModelAttribute Tutorial tutorial, BindingResult results,
			@RequestParam("categorySelection") Long categoryId,
			Model model, 
			RedirectAttributes redirectAttributes) {
    	logger.info("Entering updateTutorial method with tutorialId: {}", tutorial.getTutorialId());
		if (results.hasErrors()){
		 	List<Category> categories = categoryService.getAllCategorys();
			model.addAttribute("categories",categories);
			logger.error("Validation errors: {}", results.getAllErrors());
			return "tutorials/tutorial-update";
		}
		
		tutorialService.updateTutorial(tutorial, categoryId);
        redirectAttributes.addFlashAttribute("updated", "tutorial updated!");
        logger.info("Tutorial updated successfully with tutorialId: {}", tutorial.getTutorialId());
		return "redirect:/tutorial/all";
	}
    
   //new video max 99MB. if it had a video, deletes the old one and uploads the new one on cloudinary
    @PostMapping("/uploadvideo/{id}")
	public String uploadVideo(@PathVariable("id") Long tutorialId, @RequestParam("video") MultipartFile file, RedirectAttributes redirectAttributes) {
    	logger.info("Entering uploadVideo method with tutorialId: {}", tutorialId);
    	Tutorial tutorial = tutorialService.getTutorialById(tutorialId);
    	if(tutorial.getVideoUrl() != null && !tutorial.getVideoUrl().isEmpty()) {
    		
    		String s = tutorial.getVideoUrl();  //String split to get public id and delete it
    		String[] news = s.split("/");
    		String name = news[news.length-1];
    		String[] test = name.split("\\.");
    		String publicId = test[0];
    		cloudinaryService.deleteFile(publicId);
    		logger.info("Existing tutorial video deleted with publicId: {}", publicId);
    	}
		cloudinaryService.uploadFile(file, tutorialId);
        redirectAttributes.addFlashAttribute("updated", "tutorial video updated!");
        logger.info("New video uploaded successfully for tutorialId: {}", tutorialId);
		return "redirect:/channel/mychannel";
	}
    
    //deletes the video
    @GetMapping("/deletevideo/{id}")
	public String deleteVideo(@PathVariable("id") Long tutorialId, 
			Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
    	logger.info("Entering deleteVideo method with tutorialId: {}", tutorialId);
    	Tutorial tutorial = tutorialService.getTutorialById(tutorialId);
    	if(tutorial.getVideoUrl() != null && !tutorial.getVideoUrl().isEmpty()) {
    		
    		String s = tutorial.getVideoUrl();  //String split to get public id and delete it
    		String[] news = s.split("/");
    		String name = news[news.length-1];
    		String[] test = name.split("\\.");
    		String publicId = test[0];
    		cloudinaryService.deleteFile(publicId);
    		cloudinaryService.deleteVideoUrl(tutorialId);
    		logger.info("Tutorial video deleted with publicId: {}", publicId);
    	}
    	redirectAttributes.addFlashAttribute("deleted", "tutorial video deleted!");
    	logger.info("Tutorial video deleted successfully for tutorialId: {}", tutorialId);
		return "redirect:/channel/mychannel";
	}
    
}
