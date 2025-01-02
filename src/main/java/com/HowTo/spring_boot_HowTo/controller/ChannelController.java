package com.HowTo.spring_boot_HowTo.controller;

import java.time.LocalDate;
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

import com.HowTo.spring_boot_HowTo.model.Channel;
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
	
	//CREATE
	@GetMapping("/create")
	public String showChannelAdForm(Model model, HttpServletRequest request) {
		
		Channel channelForm = new Channel();
		channelForm.setUserid((long) -1); //TODO change dynamically after user authorization is implemented
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
    
    //GETALL
    @GetMapping("/all")
	public String showChannelList(Model model) {
		
    	List<Channel> AllChannels = channelService.getAllChannels();
		model.addAttribute("channels", AllChannels);
				
		return "/channels/channel-list";
	}
    
    
    @GetMapping("/delete/{userid}")
    public String deleteChannel(@PathVariable("userid") long userid, Model model, RedirectAttributes redirectAttributes) {
        Channel channel = channelService.getChannelById(userid);               
        channelService.delete(channel);
        redirectAttributes.addFlashAttribute("deleted", "Channel deleted!");
        return "redirect:/channel/all";
    }
	
    @GetMapping("/update/{userid}")
	public String showUpdateChannelForm(@PathVariable("userid") Long userid, 
			Model model,
			HttpServletRequest request) {
	 	Channel channel = channelService.getChannelById(userid); 
    	model.addAttribute("channel", channel);
		request.getSession().setAttribute("channelSession", channel);
		
		System.out.println("updating channel id="+ userid);
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
