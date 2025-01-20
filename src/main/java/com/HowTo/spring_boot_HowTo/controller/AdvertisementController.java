package com.HowTo.spring_boot_HowTo.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.service.AdvertisementServiceI;
import com.HowTo.spring_boot_HowTo.service.CategoryServiceI;
import com.HowTo.spring_boot_HowTo.service.CloudinaryServiceI;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/advertisement")
public class AdvertisementController {
	
	private AdvertisementServiceI advertisementService;
	private CloudinaryServiceI cloudinaryService;
	private CategoryServiceI categoryService;
	private static final Logger logger = LogManager.getLogger(GroupController.class);
	

	public AdvertisementController(AdvertisementServiceI AdvertisementService, CategoryServiceI categoryService, CloudinaryServiceI cloudinaryService) {
		super();
		this.advertisementService = AdvertisementService;
		this.categoryService = categoryService;
		this.cloudinaryService = cloudinaryService;
	}
	
	//returns site of one specific ad
	@GetMapping("/view/{id}")
	public String getAdvertisementView(@PathVariable("id") Long id, Model model) {
		
		Advertisement advertisement = advertisementService.getAdvertisementById(id); 
		model.addAttribute("advertisement", advertisement );
		logger.info("In advertisement view with id " + id);
		return "advertisements/advertisement";
	}
	
	//returns all ads site
	@GetMapping("/all")
	public String showAllAdvertisements(Model model) {
		List<Advertisement> advertisements = advertisementService.getAllAdvertisements();
		model.addAttribute("advertisements",  advertisements);
		return "advertisements/advertisement-list";
	}
	
	//shows the form to create ad
	@GetMapping("/create")
	public String showAdvertisementForm(Model model) {
		logger.info("Entering showAdvertisementForm method");
		Advertisement advertisementForm = new Advertisement();
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		advertisementForm.setCreationTime(currentTimestamp);
		logger.debug("Advertisement form created with creation time: {}", currentTimestamp);
		List<Category> categories = categoryService.getAllCategorys();
		model.addAttribute("advertisement", advertisementForm);
		model.addAttribute("categories",categories);
		logger.info("Categories retrieved and added to model: {}", categories);
		logger.info("Exiting showAdvertisementForm method");
		return "advertisements/advertisement-create";
	}
	
	//creates ad and adds it to the db
	@PostMapping("/create")
	public String addAdvertisement(@RequestParam("categorySelection") Long categoryId, @Valid @ModelAttribute Advertisement advertisement, Model model, BindingResult result) {
		logger.info("Entering addAdvertisement method with categoryId: " + categoryId);
		if (result.hasErrors()) {
			logger.error("Validation errors: {}", result.getAllErrors());
			return "advertisements/advertisement-create";
		}
		
		advertisementService.saveAdvertisement(advertisement, categoryId);
		logger.info("Advertisement saved successfully with categoryId: {}", categoryId);
		return "redirect:/advertisement/view/" + advertisement.getAdvertisementId();
	}
	
	//shows form to update ad
	@GetMapping("/update/{id}")
	public String showUpdateAdvertisementForm(@PathVariable("id") Long advertisementId, Model model) {
		logger.info("Entering showUpdateAdvertisementForm method with advertisementId: {}", advertisementId);
		Advertisement advertisement = advertisementService.getAdvertisementById(advertisementId);
		model.addAttribute("advertisement", advertisement);
		List<Category> categories = categoryService.getAllCategorys();
		model.addAttribute("categories",categories);
		logger.info("Advertisement retrieved and added to model with advertisementId: {}", advertisementId);
		return "/advertisements/advertisement-update";
	}
	
	//updates ad
	@PostMapping("/update")
	public String updateAdvertisement(@Valid @ModelAttribute Advertisement advertisement, @RequestParam("categorySelection") Long categoryId, BindingResult results, Model model,
			RedirectAttributes redirectAttributes) {
		logger.info("Entering updateAdvertisement method with advertisementId: {}", advertisement.getAdvertisementId());
		if (results.hasErrors()) {
			logger.error("Validation errors: {}", results.getAllErrors());
			return "/advertisements/advertisement-update";
		}

		advertisementService.updateAdvertisement(advertisement, categoryId);
		redirectAttributes.addFlashAttribute("updated", "advertisement updated!");
		logger.info("Advertisement updated successfully with advertisementId: {}", advertisement.getAdvertisementId());
		return "redirect:/advertisement/all";

	}
	
	//deletes a ad
	@GetMapping("/delete/{id}")
    public String deleteAdvertisement(@PathVariable("id") Long advertisementId, Model model, RedirectAttributes redirectAttributes) {
		logger.info("Entering deleteAdvertisement method with advertisementId: {}", advertisementId);
		Advertisement advertisement = advertisementService.getAdvertisementById(advertisementId);     
    	if(advertisement != null && advertisement.getVideoUrl() != null && !advertisement.getVideoUrl().isEmpty()) {
    		//if a video exists, deletes it on cloudinary with the public id
    		String s = advertisement.getVideoUrl();  //String split to get public id and delete it
    		String[] news = s.split("/");
    		String name = news[news.length-1];
    		String[] test = name.split("\\.");
    		String publicId = test[0];
    		cloudinaryService.deleteFileAdvertisement(publicId);
    		cloudinaryService.deleteVideoUrlAdvertisement(advertisementId);
    		logger.info("Advertisement video deleted with publicId: {}", publicId);
    	}
        advertisementService.delete(advertisement);
        redirectAttributes.addFlashAttribute("deleted", "Advertisement deleted!");
        logger.info("Advertisement deleted successfully with advertisementId: {}", advertisementId);
        return "redirect:/advertisement/all";
    }
    
	//uploads new video
    @PostMapping("/uploadvideo/{id}")
	public String uploadVideo(@PathVariable("id") Long advertisementId, @RequestParam("video") MultipartFile file, RedirectAttributes redirectAttributes) {
    	logger.info("Entering uploadVideo method with advertisementId: {}", advertisementId);
    	Advertisement advertisement = advertisementService.getAdvertisementById(advertisementId);
    	if(advertisement.getVideoUrl() != null && !advertisement.getVideoUrl().isEmpty()) {
    		//if video exists, deletes the previous video, then uploads new
    		String s = advertisement.getVideoUrl();  //String split to get public id and delete it
    		String[] news = s.split("/");
    		String name = news[news.length-1];
    		String[] test = name.split("\\.");
    		String publicId = test[0];
    		cloudinaryService.deleteFileAdvertisement(publicId);
    		logger.info("Existing video deleted with publicId: {}", publicId);
    	}
		cloudinaryService.uploadFileAdvertisement(file, advertisementId);
        redirectAttributes.addFlashAttribute("updated", "advertisement video updated!");
        logger.info("Video uploaded successfully for advertisementId: {}", advertisementId);
		return "redirect:/advertisement/all";
	}
    
    //delete video
    @GetMapping("/deletevideo/{id}")
	public String deleteVideo(@PathVariable("id") Long advertisementId, 
			Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
    	logger.info("Entering deleteVideo method with advertisementId: {}", advertisementId);
    	Advertisement advertisement = advertisementService.getAdvertisementById(advertisementId);
    	if(advertisement.getVideoUrl() != null && !advertisement.getVideoUrl().isEmpty()) {
    		
    		String s = advertisement.getVideoUrl();  //String split to get public id and delete it
    		String[] news = s.split("/");
    		String name = news[news.length-1];
    		String[] test = name.split("\\.");
    		String publicId = test[0];
    		cloudinaryService.deleteFileAdvertisement(publicId);
    		cloudinaryService.deleteVideoUrlAdvertisement(advertisementId);
    		logger.info("Advertisement video deleted with publicId: {}", publicId);
    	}
    	redirectAttributes.addFlashAttribute("deleted", "advertisement video deleted!");
    	logger.info("Video deleted successfully for advertisementId: {}", advertisementId);
		return "redirect:/advertisement/all";
	}
	
}
