 package com.HowTo.spring_boot_HowTo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.config.MyUserDetails;
import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.service.ChannelServiceI;
import com.HowTo.spring_boot_HowTo.validator.ChannelValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/channel")
public class ChannelController {

	private ChannelServiceI channelService;
	
	
	public ChannelController(ChannelServiceI channelService) {
		super();
		this.channelService = channelService;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new ChannelValidator());
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
	public String showChannelList(@PathVariable("id") Long channelid ,Model model) {
		
    	Channel channel = channelService.getChannelById(channelid);
    	List<Tutorial> tutorials = channel.getTutorials();
    	model.addAttribute("channel", channel);
		model.addAttribute("tutorials", tutorials);
				
		return "/channels/channel";
	}
	
	//CREATE
	@GetMapping("/create")
	public String showChannelAdForm(Model model, HttpServletRequest request) {
		
		Channel channelForm = new Channel();
		System.out.println(getCurrentUserId());
		channelForm.setChannelId(getCurrentUserId()); //TODO change dynamically after channel authorization is implemented
		LocalDate date= LocalDate.now();
		channelForm.setCreationDate(date);
		
		request.getSession().setAttribute("channelSession", channelForm);
		model.addAttribute("channel", channelForm);
				
		return "channels/channel-create";
	}
	
    @PostMapping("/create")
    public String addChannel(@Valid @ModelAttribute Channel channel, 
    		BindingResult result, 
    		Model model,
    		RedirectAttributes redirectAttributes) {
    	
    	if (result.hasErrors()) {
    		System.out.println(result.getAllErrors().toString());
            return "/channels/channel-create";
        }

    	
    	channelService.saveChannel(channel);
        redirectAttributes.addFlashAttribute("created", "Channel created!");
        
        return "redirect:/channel/all";
    }
    
	@GetMapping(value = {"", "/all"})
	public String showChannelList(Model model, @RequestParam(required = false) String keyword,
			@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false,
			defaultValue = "5") int size) {
		try {
			
			List<Channel> channels = new ArrayList<Channel>();

			 //the first page is 1 for the channel, 0 for the database.
			 Pageable paging = PageRequest.of(page - 1, size);
			 Page<Channel> pageChannel;
			 //getting the page from the database….
			 pageChannel = channelService.getAllChannels(keyword, paging);

			 model.addAttribute("keyword", keyword);

			 channels = pageChannel.getContent();
			 model.addAttribute("channels", channels);
			 //here are the variables for the paginator in the channel-all view
			 model.addAttribute("entitytype", "channel");
			 model.addAttribute("currentPage", pageChannel.getNumber() + 1);
			 model.addAttribute("totalItems", pageChannel.getTotalElements());
			 model.addAttribute("totalPages", pageChannel.getTotalPages());
			 model.addAttribute("pageSize", size);
			 
		} catch (Exception e){
			model.addAttribute("message", e.getMessage());
		}
		return "/channels/channel-list";
	}
    
    
    @GetMapping("/delete/{channelId}")
    public String deleteChannel(@PathVariable("channelId") long channelId, Model model, RedirectAttributes redirectAttributes) {
        Channel channel = channelService.getChannelById(channelId);               
        channelService.delete(channel);
        redirectAttributes.addFlashAttribute("deleted", "Channel deleted!");
        return "redirect:/channel/all";
    }
	
    @GetMapping("/update/{channelId}")
	public String showUpdateChannelForm(@PathVariable("channelId") Long channelId, 
			Model model,
			HttpServletRequest request) {
	 	Channel channel = channelService.getChannelById(channelId); 
    	model.addAttribute("channel", channel);
		request.getSession().setAttribute("channelSession", channel);
		
		System.out.println("updating channel id="+ channelId);
		return "/channels/channel-update";
	}
    
    
    @PostMapping("/update")
	public String updateChannel(@Valid @ModelAttribute Channel channel,
			BindingResult results,
			Model model, 
			RedirectAttributes redirectAttributes) {
		
				
		if (results.hasErrors()){
			
			return "/channels/channel-update";
		}
       
		channelService.updateChannel(channel);
        redirectAttributes.addFlashAttribute("updated", "channel updated!");
		return "redirect:/channel/all";
		
	}
}
