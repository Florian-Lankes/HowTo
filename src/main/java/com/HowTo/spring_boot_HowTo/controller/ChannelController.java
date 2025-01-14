 package com.HowTo.spring_boot_HowTo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.OnInformChannelEvent.OnInformChannelEvent;
import com.HowTo.spring_boot_HowTo.changepasswordloggedin.OnChangePasswordLoggedInEvent;
import com.HowTo.spring_boot_HowTo.config.MyUserDetails;
import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.WatchLater;
import com.HowTo.spring_boot_HowTo.service.ChannelServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;
import com.HowTo.spring_boot_HowTo.validator.ChannelValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/channel")
public class ChannelController {

	private ChannelServiceI channelService;
	
	private UserServiceI userService;
	
	@Autowired
    private ApplicationEventPublisher eventPublisher;
	
	public ChannelController(ChannelServiceI channelService, UserServiceI userService) {
		super();
		this.channelService = channelService;
		this.userService = userService;
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
		User user = (User) authentication.getPrincipal();
		return user.getUserId();
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

    	
    	channelService.saveChannel(channel, getCurrentUserId());
        redirectAttributes.addFlashAttribute("created", "Channel created!");
        
        return "redirect:/logout";
    }
    
   @GetMapping(value = {"/", "/all"})
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
    	User u = userService.getUserById(getCurrentUserId());
    	boolean admin= userService.checkAdmin(u);
    	System.out.println(admin);
    	if(channelId  !=getCurrentUserId() && !admin) {
    		System.out.println("Not Admin or Channelowner");
    		redirectAttributes.addFlashAttribute("failed", "not owner!!");
    		return "redirect:/home";
    	}
        Channel channel = channelService.getChannelById(channelId);               
        channelService.delete(channel);
        redirectAttributes.addFlashAttribute("deleted", "Channel deleted!");
        
        if(admin) {
        return "redirect:/channel/all";
        }
        else {
        	return "redirect:/logout";
        }
    }
	
    @GetMapping("/update/{channelId}")
	public String showUpdateChannelForm(@PathVariable("channelId") Long channelId, 
			Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
    	User u = userService.getUserById(getCurrentUserId());
    	boolean admin= userService.checkAdmin(u);
    	System.out.println(admin);
    	if(channelId  !=getCurrentUserId() && !admin) {
    		System.out.println("Not Admin or Channelowner");
    		redirectAttributes.addFlashAttribute("failed", "not owner!!");
    		return "redirect:/home";
    	}
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
    
    @PostMapping("/subscribe")
    public String subscribeChannel(@Valid @ModelAttribute Channel channel,
		BindingResult results,
		Model model, 
		RedirectAttributes redirectAttributes) {
    	
    	channelService.subscribeChannel(channel, getCurrentUserId());
    	User u = userService.getUserById(channel.getChannelId());
    	User current = userService.getUserById(getCurrentUserId());
    	eventPublisher.publishEvent(new OnInformChannelEvent(u, current.getUsername()));
    	
    	redirectAttributes.addFlashAttribute("subscribed", "subscribed!");
    	return "redirect:/channel/view/"+channel.getChannelId();
    }
    
    @PostMapping("/unsubscribe")
    public String unsubscribeChannel(@Valid @ModelAttribute Channel channel,
		BindingResult results,
		Model model, 
		RedirectAttributes redirectAttributes) {
    	
    	channelService.unsubscribeChannel(channel, getCurrentUserId());
    	redirectAttributes.addFlashAttribute("unsubscribed", "unsubscribed!");
    	return "redirect:/channel/view/"+channel.getChannelId();
    }

    @GetMapping("/subscriberlist/{id}")
   	public String showChannelSubscriber(@PathVariable("id") Long channelId, 
   			Model model,
   			HttpServletRequest request) {
   	 	Channel channel = channelService.getChannelById(channelId); 
       	model.addAttribute("channel", channel);
   		request.getSession().setAttribute("channelSession", channel);
   		
   		System.out.println("get subscriberlist="+ channelId);
   		return "/channels/subscriber-list";
   	}
    
	@GetMapping("/subscribed")
	public String showSubscribedChannelList(Model model, HttpServletRequest request) {
		User user = userService.getUserById(getCurrentUserId());
		List<Channel> channels = user.getSubscribedChannels(); 
		model.addAttribute("channels", channels );
				
		return "/subscribedChannel";
	}
}
