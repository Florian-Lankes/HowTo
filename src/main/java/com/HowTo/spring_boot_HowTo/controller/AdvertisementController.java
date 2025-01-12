package com.HowTo.spring_boot_HowTo.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

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

	public AdvertisementController(AdvertisementServiceI AdvertisementService, CategoryServiceI categoryService, CloudinaryServiceI cloudinaryService) {
		super();
		this.advertisementService = AdvertisementService;
		this.categoryService = categoryService;
		this.cloudinaryService = cloudinaryService;
	}
	
	@GetMapping("/view/{id}")
	public String getAdvertisementView(@PathVariable("id") Long id, Model model) {
		
		Advertisement advertisement = advertisementService.getAdvertisementById(id); 
		model.addAttribute("advertisement", advertisement );

		return "advertisements/advertisement";
	}
	
	@GetMapping("/all")
	public String showAllAdvertisements(Model model) {
		List<Advertisement> advertisements = advertisementService.getAllAdvertisements();
		model.addAttribute("advertisements",  advertisements);
		return "advertisements/advertisement-list";
	}
	
	@GetMapping("/create")
	public String showAdvertisementForm(Model model) {

		Advertisement advertisementForm = new Advertisement();
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		advertisementForm.setCreationTime(currentTimestamp);
		List<Category> categories = categoryService.getAllCategorys();
		model.addAttribute("advertisement", advertisementForm);
		model.addAttribute("categories",categories);
		return "advertisements/advertisement-create";
	}
	
	@PostMapping("/create")
	public String addAdvertisement(@RequestParam("categorySelection") Long categoryId, @Valid @ModelAttribute Advertisement advertisement, Model model, BindingResult result) {
		
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors().toString());
			return "advertisements/advertisement-create";
		}
		
		advertisementService.saveAdvertisement(advertisement, categoryId);
		return "redirect:/advertisement/all";
	}
	
	@GetMapping("/update/{id}")
	public String showUpdateAdvertisementForm(@PathVariable("id") Long advertisementId, Model model) {
		Advertisement advertisement = advertisementService.getAdvertisementById(advertisementId);
		model.addAttribute("advertisement", advertisement);
		List<Category> categories = categoryService.getAllCategorys();
		model.addAttribute("categories",categories);

		return "/advertisements/advertisement-update";
	}

	@PostMapping("/update")
	public String updateAdvertisement(@Valid @ModelAttribute Advertisement advertisement, @RequestParam("categorySelection") Long categoryId, BindingResult results, Model model,
			RedirectAttributes redirectAttributes) {

		if (results.hasErrors()) {

			return "/advertisements/advertisement-update";
		}

		advertisementService.updateAdvertisement(advertisement, categoryId);
		redirectAttributes.addFlashAttribute("updated", "advertisement updated!");
		return "redirect:/advertisement/all";

	}
	
	@GetMapping("/delete/{id}")
    public String deleteAdvertisement(@PathVariable("id") Long advertisementId, Model model, RedirectAttributes redirectAttributes) {
        Advertisement advertisement = advertisementService.getAdvertisementById(advertisementId);     
    	if(advertisement != null && advertisement.getVideoUrl() != null && !advertisement.getVideoUrl().isEmpty()) {
    		
    		String s = advertisement.getVideoUrl();  //String split to get public id and delete it
    		String[] news = s.split("/");
    		String name = news[news.length-1];
    		String[] test = name.split("\\.");
    		String publicId = test[0];
    		cloudinaryService.deleteFileAdvertisement(publicId);
    		cloudinaryService.deleteVideoUrlAdvertisement(advertisementId);
    	}
        advertisementService.delete(advertisement);
        redirectAttributes.addFlashAttribute("deleted", "Advertisement deleted!");
        return "redirect:/advertisement/all";
    }
    
    @PostMapping("/uploadvideo/{id}")
	public String uploadVideo(@PathVariable("id") Long advertisementId, @RequestParam("video") MultipartFile file, RedirectAttributes redirectAttributes) {
    	
    	Advertisement advertisement = advertisementService.getAdvertisementById(advertisementId);
    	if(advertisement.getVideoUrl() != null && !advertisement.getVideoUrl().isEmpty()) {
    		
    		String s = advertisement.getVideoUrl();  //String split to get public id and delete it
    		String[] news = s.split("/");
    		String name = news[news.length-1];
    		String[] test = name.split("\\.");
    		String publicId = test[0];
    		cloudinaryService.deleteFileAdvertisement(publicId);
    	}
		cloudinaryService.uploadFileAdvertisement(file, advertisementId);
        redirectAttributes.addFlashAttribute("updated", "advertisement video updated!");
		return "redirect:/advertisement/all";
	}
    
    @GetMapping("/deletevideo/{id}")
	public String deleteVideo(@PathVariable("id") Long advertisementId, 
			Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
    	Advertisement advertisement = advertisementService.getAdvertisementById(advertisementId);
    	if(advertisement.getVideoUrl() != null && !advertisement.getVideoUrl().isEmpty()) {
    		
    		String s = advertisement.getVideoUrl();  //String split to get public id and delete it
    		String[] news = s.split("/");
    		String name = news[news.length-1];
    		String[] test = name.split("\\.");
    		String publicId = test[0];
    		cloudinaryService.deleteFileAdvertisement(publicId);
    		cloudinaryService.deleteVideoUrlAdvertisement(advertisementId);
    	}
    	redirectAttributes.addFlashAttribute("deleted", "advertisement video deleted!");
		return "redirect:/advertisement/all";
	}
	
}
