 package com.HowTo.spring_boot_HowTo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.OnInformChannelEvent.OnInformChannelEvent;
import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.Wallet;
import com.HowTo.spring_boot_HowTo.service.ChannelServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;
import com.HowTo.spring_boot_HowTo.service.WalletServiceI;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/channel")
public class ChannelController {

	private ChannelServiceI channelService;
	
	private UserServiceI userService;
	
	private WalletServiceI walletService;
	@Autowired
    private ApplicationEventPublisher eventPublisher;
	private static final Logger logger = LogManager.getLogger(GroupController.class);
	
	
	public ChannelController(ChannelServiceI channelService, UserServiceI userService, WalletServiceI walletService) {
		super();
		this.channelService = channelService;
		this.userService = userService;
		this.walletService = walletService;
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
	
	//show a channel with their tutorials
	@GetMapping("/view/{id}")
	public String showChannelList(@PathVariable("id") Long channelid ,Model model) {
		logger.info("Entering showChannelList method with channelId: {}", channelid);
    	Channel channel = channelService.getChannelById(channelid);
    	User current = userService.getUserById(getCurrentUserId());
    	if(channel.getSubscribedFromUserList().contains(current)) {
    		model.addAttribute("abonniert", true);
    		logger.info("User is subscribed to the channel");
    	}else {
    		model.addAttribute("abonniert", false);
    		logger.info("User is not subscribed to the channel");
    	}
    	List<Tutorial> tutorials = channel.getTutorials();
    	Wallet wallet = walletService.getWalletById(getCurrentUserId());
		model.addAttribute("wallet", wallet);
		model.addAttribute("CurrentUserId", getCurrentUserId());
    	model.addAttribute("channel", channel);
		model.addAttribute("tutorials", tutorials);
				
		logger.info("Channel and tutorials retrieved and added to model");
		return "channels/channel";
	}
	
	//shows your channel page, where you upload and edit tutorials
	@GetMapping("/mychannel")
	public String showMyChannel(Model model) {
		logger.info("Entering showMyChannel method");
    	Channel channel = channelService.getChannelById(getCurrentUserId());
    	if(channel != null) {
	    	List<Tutorial> tutorials = channel.getTutorials();
	    	model.addAttribute("channel", channel);
			model.addAttribute("tutorials", tutorials);
			logger.info("Channel and tutorials retrieved and added to model");
			return "channels/mychannel";
		}
    	else {
    		logger.warn("No channel found for current user, redirecting to /user/my");
    		return "redirect:/user/my";
    	}
	}
	
	
	//create form
	@GetMapping("/create")
	public String showChannelAdForm(Model model, HttpServletRequest request) {
		logger.info("Entering showChannelAdForm method");
		Channel channelForm = new Channel();
		channelForm.setChannelId(getCurrentUserId()); //TODO change dynamically after channel authorization is implemented
		LocalDate date= LocalDate.now();
		channelForm.setCreationDate(date);
		
		request.getSession().setAttribute("channelSession", channelForm);
		model.addAttribute("channel", channelForm);
		logger.info("Channel form created and added to model");
		return "channels/channel-create";
	}
	
	//create channel and new wallet if it doenst exist yet
    @PostMapping("/create")
    public String addChannel(@Valid @ModelAttribute Channel channel, 
    		BindingResult result, 
    		Model model,
    		RedirectAttributes redirectAttributes) {
    	logger.info("Entering addChannel method with channel: {}", channel);
    	if (result.hasErrors()) {
    		logger.error("Validation errors: {}", result.getAllErrors());
    		logger.info("User with id " + getCurrentUserId() + " failed to create channel");
            return "channels/channel-create";
        }
    	
    	if(walletService.getWalletById(getCurrentUserId()) == null) {
	    	Wallet wallet = new Wallet();
	    	walletService.saveWallet(wallet, getCurrentUserId());
	    	logger.info("New wallet created for user with id {}", getCurrentUserId());
    	}
    	
    	channelService.saveChannel(channel, getCurrentUserId());
        redirectAttributes.addFlashAttribute("created", "Channel created!");
        logger.info("User with id {} created channel with id {}", getCurrentUserId(), channel.getChannelId());
        return "redirect:/logout";
    }
    
    //get all channels (pagination)
   @GetMapping(value = {"/", "/all"})
	public String showChannelList(Model model, @RequestParam(required = false) String keyword,
			@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false,
			defaultValue = "5") int size) {
	   	logger.info("Entering showChannelList method with keyword: {}, page: {}, size: {}", keyword, page, size);
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
			 logger.info("Channels retrieved and added to model");
			 
		} catch (Exception e){
			logger.error("Exception occurred while retrieving channels: {}", e.getMessage());
			model.addAttribute("message", e.getMessage());
		}
		return "channels/channel-list";
	}
    
    //delete a channel if he is admin or owner
    @GetMapping("/delete/{channelId}")
    public String deleteChannel(@PathVariable("channelId") long channelId, Model model, RedirectAttributes redirectAttributes) {
    	logger.info("Entering deleteChannel method with channelId: {}", channelId);
    	User u = userService.getUserById(getCurrentUserId());
    	boolean admin= userService.checkAdmin(u);
    	System.out.println(admin);
    	if(channelId  !=getCurrentUserId() && !admin) {
    		logger.warn("User with id {} is not admin or channel owner, cannot delete channel with id {}", getCurrentUserId(), channelId);
    		redirectAttributes.addFlashAttribute("failed", "not owner!!");
    		return "redirect:/home";
    	}
        Channel channel = channelService.getChannelById(channelId);               
        channelService.delete(channel);
        redirectAttributes.addFlashAttribute("deleted", "Channel deleted!");
        logger.info("User with id {} deleted channel with id {}", getCurrentUserId(), channelId);
        
        if(admin) {
        return "redirect:/channel/all";
        }
        else {
        	return "redirect:/logout";
        }
    }
	
    //update Channel form
    @GetMapping("/update/{channelId}")
	public String showUpdateChannelForm(@PathVariable("channelId") Long channelId, 
			Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
    	logger.info("Entering showUpdateChannelForm method with channelId: {}", channelId);
    	User u = userService.getUserById(getCurrentUserId());
    	boolean admin= userService.checkAdmin(u);
    	logger.debug("Admin status of user: {}", admin);
    	if(channelId  !=getCurrentUserId() && !admin) {
    		logger.warn("User with id {} is not admin or channel owner, cannot update channel with id {}", getCurrentUserId(), channelId);
    		redirectAttributes.addFlashAttribute("failed", "not owner!!");
    		return "redirect:/home";
    	}
	 	Channel channel = channelService.getChannelById(channelId); 
    	model.addAttribute("channel", channel);
		request.getSession().setAttribute("channelSession", channel);
		logger.info("Channel retrieved and added to model with channelId: {}", channelId);
		return "channels/channel-update";
	}
    
    //update channel
    @PostMapping("/update")
	public String updateChannel(@Valid @ModelAttribute Channel channel,
			BindingResult results,
			Model model, 
			RedirectAttributes redirectAttributes) {
		
    	logger.info("Entering updateChannel method with channel: {}", channel);		
		if (results.hasErrors()){
			logger.error("Validation errors: {}", results.getAllErrors());
			return "channels/channel-update";
		}
       
		Channel u =channelService.updateChannel(channel);
        redirectAttributes.addFlashAttribute("updated", "channel updated!");
        logger.info("User with id {} updated channel with id {}", getCurrentUserId(), u.getChannelId());
        if(u.getChannelId() != getCurrentUserId()) {
        	return "redirect:/channel/all";
        }else {
        	return "redirect:/channel/mychannel";
        }
	}
    
    //subscribe to channel and notify channel
    @PostMapping("/subscribe")
    public String subscribeChannel(@Valid @ModelAttribute Channel channel,
		BindingResult results,
		Model model, 
		RedirectAttributes redirectAttributes) {
    	logger.info("Entering subscribeChannel method with channelId: {}", channel.getChannelId());
    	channelService.subscribeChannel(channel, getCurrentUserId());
    	User u = userService.getUserById(channel.getChannelId());
    	User current = userService.getUserById(getCurrentUserId());
    	
    	//this event sends a mail to the subscribed channel and informs him
    	eventPublisher.publishEvent(new OnInformChannelEvent(u, current.getUsername()));
    	redirectAttributes.addFlashAttribute("subscribed", "subscribed!");
    	logger.info("User with id {} subscribed to channel with id {}", getCurrentUserId(), channel.getChannelId());
    	return "redirect:/channel/view/"+channel.getChannelId();
    }
    
    //unsubscribe from channel
    @PostMapping("/unsubscribe")
    public String unsubscribeChannel(@Valid @ModelAttribute Channel channel,
		BindingResult results,
		Model model, 
		RedirectAttributes redirectAttributes) {
    	logger.info("Entering unsubscribeChannel method with channelId: {}", channel.getChannelId());
    	channelService.unsubscribeChannel(channel, getCurrentUserId());
    	redirectAttributes.addFlashAttribute("unsubscribed", "unsubscribed!");
    	logger.info("User with id {} unsubscribed from channel with id {}", getCurrentUserId(), channel.getChannelId());
    	return "redirect:/channel/view/"+channel.getChannelId();
    }

    //shows all subscriber from the channel
    @GetMapping("/subscriberlist/{id}")
   	public String showChannelSubscriber(@PathVariable("id") Long channelId, 
   			Model model,
   			HttpServletRequest request) {
    	logger.info("Entering showChannelSubscriber method with channelId: {}", channelId);
   	 	Channel channel = channelService.getChannelById(channelId); 
       	model.addAttribute("channel", channel);
   		request.getSession().setAttribute("channelSession", channel);
   		
   		logger.info("Channel retrieved and added to model with channelId: {}", channelId); 
   		logger.debug("get subscriberlist={}", channelId);
   		return "channels/subscriber-list";
   	}
    
    //shows all channels you subscribed
	@GetMapping("/subscribed")
	public String showSubscribedChannelList(Model model, HttpServletRequest request) {
		logger.info("Entering showSubscribedChannelList method");
		User user = userService.getUserById(getCurrentUserId());
		List<Channel> channels = user.getSubscribedChannels();
		model.addAttribute("channels", channels );
		logger.info("Subscribed channels retrieved and added to model for userId: {}", getCurrentUserId()); 
		logger.debug("Subscribed channels: {}", channels);
		return "subscribedChannel";
	}
	
	//shows all tutorials from the channel you subscribed
	@GetMapping("/subscribed/tutorials")
	public String HowToView(Model model) {
		logger.info("Entering showSubscribedChannelTutorials method");
		User user = userService.getUserById(getCurrentUserId());
		List<Channel> channels = user.getSubscribedChannels();
		List<Tutorial> tutorials = new ArrayList<Tutorial>();
		for (Channel c : channels) {
			tutorials.addAll(c.getTutorials());
		}

			
		model.addAttribute("tutorial", tutorials);
		logger.info("Tutorials retrieved and added to model");
		return "channels/subscribed-tutorials";
	}
}
