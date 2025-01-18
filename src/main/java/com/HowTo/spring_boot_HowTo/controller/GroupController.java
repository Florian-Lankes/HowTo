package com.HowTo.spring_boot_HowTo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.model.Message;
import com.HowTo.spring_boot_HowTo.model.MessageDTO;
import com.HowTo.spring_boot_HowTo.model.MessageDTOMapper;
import com.HowTo.spring_boot_HowTo.model.MessageType;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.service.GroupServiceI;
import com.HowTo.spring_boot_HowTo.service.MessageServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/group")
public class GroupController {
	
	private GroupServiceI groupService;
	private UserServiceI userService;
	private MessageServiceI messageService;
	private final SimpMessageSendingOperations messageTemplate;
	//private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
	private static final Logger logger = LogManager.getLogger(GroupController.class);
	
	public GroupController(GroupServiceI groupService, UserServiceI userService, MessageServiceI messageService, 
			SimpMessageSendingOperations messageTemplate) {
		super();
		this.groupService = groupService;
		this.userService = userService;
		this.messageService = messageService;
		this.messageTemplate = messageTemplate;
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
	
	//CREATE
	@GetMapping("/create")
	public String showGroupAdForm(Model model, HttpServletRequest request) {
		logger.info("Entering showGroupAdForm method");
		Group groupForm = new Group();
		//groupForm.setGroupId(null); //TODO change dynamically after user authorization is implemented
		LocalDate date= LocalDate.now();
		groupForm.setCreationDate(date);
		
		request.getSession().setAttribute("groupSession", groupForm);
		model.addAttribute("group", groupForm);	
		logger.info("Group form created and added to model");
		return "/groups/group-create";
	}
	
    @PostMapping("/create")
    public String addGroup(@Valid @ModelAttribute Group group, 
    		BindingResult result, 
    		Model model,
    		RedirectAttributes redirectAttributes) {
    	logger.info("Entering addGroup method with group: {}", group);
    	if (result.hasErrors()) {
    		logger.error("Validation errors: {}", result.getAllErrors()); 
    		logger.info("User with id {} failed to create group", getCurrentUserId());
            return "/groups/group-create";
        }	
    	Group r= groupService.saveGroup(group, getCurrentUserId());
        redirectAttributes.addFlashAttribute("created", "Group created!");
        groupService.joinGroup(r, getCurrentUserId()); //doenst wanna join the group after creating
        
        Message createMessage = new Message();
  		createMessage.setContent("");
  		createMessage.setMessageType(MessageType.CREATE);
  		createMessage.setMessageGroup(r);
  		createMessage.setMessageOwner(userService.getUserById(getCurrentUserId()));
  		messageService.saveMessage(createMessage);
  		MessageDTO createMessageDTO = MessageDTOMapper.toMessageDTO(createMessage);
  		messageTemplate.convertAndSend("/topic/group/" + group.getGroupId(), createMessageDTO);
        
        redirectAttributes.addFlashAttribute("added", "Group created!");
        logger.info("User with id {} created group with id {}", getCurrentUserId(), r.getGroupId());
        return "redirect:/group/all";
    }

    //GETALL
    @GetMapping(value = { "/", "/all" })
	public String showGroupList(Model model, @RequestParam(required = false) String keyword,
			@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false,
			defaultValue = "5") int size) {
    		logger.info("Entering showGroupList method with keyword: {}, page: {}, size: {}", keyword, page, size);
    		try {
			
			List<Group> groups = new ArrayList<Group>();

			 //the first page is 1 for the channel, 0 for the database.
			 Pageable paging = PageRequest.of(page - 1, size);
			 Page<Group> pageGroup;
			 //getting the page from the database….
			 pageGroup = groupService.getAllGroups(keyword, paging);

			 model.addAttribute("keyword", keyword);

			 groups = pageGroup.getContent();
			 model.addAttribute("groups", groups);
			 //here are the variables for the paginator in the channel-all view
			 model.addAttribute("entitytype", "group");
			 model.addAttribute("currentPage", pageGroup.getNumber() + 1);
			 model.addAttribute("totalItems", pageGroup.getTotalElements());
			 model.addAttribute("totalPages", pageGroup.getTotalPages());
			 model.addAttribute("pageSize", size);
			 logger.info("Groups retrieved and added to model");
			 
		} catch (Exception e){
			model.addAttribute("message", e.getMessage());
		}
		List<Group> joinedGroups = userService.getUserById(getCurrentUserId()).getJoinedGroups();
		List<Group> ownedGroups = userService.getUserById(getCurrentUserId()).getOwnedGroups();
			
		model.addAttribute("ownedGroups", ownedGroups);
		model.addAttribute("joinedGroups", joinedGroups);
		logger.info("Owned and joined groups retrieved for userId: {}", getCurrentUserId());
		return "/groups/group-list";
	}
    
    @GetMapping("/delete/{id}")
    public String deleteGroup(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
    	logger.info("Entering deleteGroup method with groupId: {}", id);
        Group group = groupService.getGroupById(id);    
        if(group.getGroupOwner().getUserId() != getCurrentUserId()) {
        	logger.warn("User with id {} is not owner of group with id {}, deletion failed", getCurrentUserId(), id);
   	 		redirectAttributes.addFlashAttribute("failed", "not owner!!");
   	 		return "redirect:/group/all";
   	 	}
        groupService.delete(group);
        redirectAttributes.addFlashAttribute("deleted", "Group deleted!");
        logger.info("User with id {} deleted group with id {}", getCurrentUserId(), id);
        return "redirect:/group/all";
    }
    
    @GetMapping("/update/{id}")
   	public String showUpdateGroupForm(@PathVariable("id") Long id, 
   			Model model,
   			RedirectAttributes redirectAttributes) {
    	logger.info("Entering showUpdateGroupForm method with groupId: {}", id);
   	 	Group group = groupService.getGroupById(id); 
   	 	if(group.getGroupOwner().getUserId() != getCurrentUserId()) {
   	 		redirectAttributes.addFlashAttribute("failed", "not owner!!");
	   	 	logger.warn("User with id {} is not owner of group with id {}, update failed", getCurrentUserId(), id);
	   	 	return "redirect:/group/all";
   	 	}
       	model.addAttribute("group", group);
       	logger.info("Group retrieved and added to model with groupId: {}", id);
   		return "/groups/group-update";
   	}
       
       
    @PostMapping("/update")
   	public String updateGroup(@Valid @ModelAttribute Group group,
   			BindingResult results,
   			Model model, 
   			RedirectAttributes redirectAttributes) {
    	logger.info("Entering updateGroup method with group: {}", group);
	   if(group.getGroupOwner().getUserId() != getCurrentUserId()) {
		   logger.warn("User with id {} is not owner of group with id {}, update failed", getCurrentUserId(), group.getGroupId());
  	 		redirectAttributes.addFlashAttribute("failed", "not owner!!");
  	 		return "/groups/group-all";
  	 	}	
       else if (results.hasErrors()){
    	   logger.error("Validation errors: {}", results.getAllErrors());
   			return "/groups/group-update";
   		}
          
   		groupService.updateGroup(group);
        redirectAttributes.addFlashAttribute("updated", "group updated!");
        logger.info("User with id {} updated group with id {}", getCurrentUserId(), group.getGroupId());
   		return "redirect:/group/all";
   		
   	}
       
//       @GetMapping("/detail/{id}")
//      	public String showDetailGroup(@PathVariable("id") Long id, 
//      			Model model,
//      			HttpServletRequest request,
//      			RedirectAttributes redirectAttributes) {
//      	 	Group group = groupService.getGroupById(id); 
//          	model.addAttribute("group", group);
//      		request.getSession().setAttribute("groupSession", group);
//      		
//      		System.out.println("detail of group id="+ id);
//      		return "/groups/group-detail";
//      	}
       
       @GetMapping("/join/{id}")
      	public String joinGroup(@PathVariable("id") Long id,
      			Model model, 
      			RedirectAttributes redirectAttributes) {
    	   	logger.info("Entering joinGroup method with groupId: {}", id);
    		List<Group> joinedgroups = userService.getUserById(getCurrentUserId()).getJoinedGroups();
    		Group realgroup = groupService.getGroupById(id);
      		if(!joinedgroups.contains(realgroup)) { // User already in group
      			Message joinMessage = new Message();
          		joinMessage.setContent("");
          		joinMessage.setMessageType(MessageType.JOIN);
          		joinMessage.setMessageGroup(realgroup);
          		joinMessage.setMessageOwner(userService.getUserById(getCurrentUserId()));
          		messageService.saveMessage(joinMessage);
          		MessageDTO joinMessageDTO = MessageDTOMapper.toMessageDTO(joinMessage);
          		messageTemplate.convertAndSend("/topic/group/" + realgroup.getGroupId(), joinMessageDTO);	
      		}
      		
      		groupService.joinGroup(realgroup, getCurrentUserId());
            redirectAttributes.addFlashAttribute("joined", "group joined!");
            logger.info("User with id {} joined group with id {}", getCurrentUserId(), id);
      		return "redirect:/group/all";
       }
       
       @GetMapping("/leave/{id}")
     	public String leaveGroup(@PathVariable("id") Long id,
     			Model model, 
     			RedirectAttributes redirectAttributes) {
    	   	logger.info("Entering leaveGroup method with groupId: {}", id);
	 		List<Group> joinedgroups = userService.getUserById(getCurrentUserId()).getJoinedGroups();
			Group realgroup = groupService.getGroupById(id);
     		if(joinedgroups.contains(realgroup)) {
     			Message leaveMessage = new Message();
         		leaveMessage.setContent("");
         		leaveMessage.setMessageType(MessageType.LEAVE);
         		leaveMessage.setMessageGroup(realgroup);
         		leaveMessage.setMessageOwner(userService.getUserById(getCurrentUserId()));
          		messageService.saveMessage(leaveMessage);
          		MessageDTO leaveMessageDTO = MessageDTOMapper.toMessageDTO(leaveMessage);
          		messageTemplate.convertAndSend("/topic/group/" + realgroup.getGroupId(), leaveMessageDTO);	
     		}
     		
     		groupService.leaveGroup(realgroup, getCurrentUserId());
            redirectAttributes.addFlashAttribute("left", "group left!");
            logger.info("User with id {} left group with id {}", getCurrentUserId(), id);
     		return "redirect:/group/all";
      }
       
}
