package com.HowTo.spring_boot_HowTo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.config.MyUserDetails;
import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.service.GroupServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/group")
public class GroupController {
	
	private GroupServiceI groupService;
	private UserServiceI userService;
	
	public GroupController(GroupServiceI groupService, UserServiceI userService) {
		super();
		this.groupService = groupService;
		this.userService = userService;
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
		
		Group groupForm = new Group();
		//groupForm.setGroupId(null); //TODO change dynamically after user authorization is implemented
		LocalDate date= LocalDate.now();
		groupForm.setCreationDate(date);
		
		request.getSession().setAttribute("groupSession", groupForm);
		model.addAttribute("group", groupForm);
				
		return "/groups/group-create";
	}
	
    @PostMapping("/create")
    public String addGroup(@Valid @ModelAttribute Group group, 
    		BindingResult result, 
    		Model model,
    		RedirectAttributes redirectAttributes) {

    	
    	if (result.hasErrors()) {
    		System.out.println(result.getAllErrors().toString());
            return "/groups/group-create";
        }

    	
    	Group r= groupService.saveGroup(group, getCurrentUserId());
    	System.out.println(r.getGroupId());
        redirectAttributes.addFlashAttribute("created", "Group created!");
        groupService.joinGroup(r, getCurrentUserId()); //doenst wanna join the group after creating
        redirectAttributes.addFlashAttribute("joined", "Group joined!");
        
        
        return "redirect:/group/all";
    }

    //GETALL
    @GetMapping("/all")
	public String showGroupList(Model model) {
		
    	List<Group> AllGroups = groupService.getAllGroups();
		model.addAttribute("groups", AllGroups);
		List<Group> joinedGroups = userService.getUserById(getCurrentUserId()).getJoinedGroups();
				
		model.addAttribute("joinedGroups", joinedGroups);
		return "/groups/group-list";
	}
    
    @GetMapping("/delete/{id}")
    public String deleteGroup(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        Group group = groupService.getGroupById(id);    
        if(group.getGroupOwner().getUserId() != getCurrentUserId()) {
   	 		System.out.println("wrong user!");
   	 		
   	 		redirectAttributes.addFlashAttribute("failed", "not owner!!");
   	 		return "redirect:/group/all";
   	 	}
        groupService.delete(group);
        redirectAttributes.addFlashAttribute("deleted", "Group deleted!");
        return "redirect:/group/all";
    }
    
    @GetMapping("/update/{id}")
   	public String showUpdateGroupForm(@PathVariable("id") Long id, 
   			Model model,
   			HttpServletRequest request,
   			RedirectAttributes redirectAttributes) {
   	 	Group group = groupService.getGroupById(id); 
   	 	if(group.getGroupOwner().getUserId() != getCurrentUserId()) {
   	 		System.out.println("wrong user!");
   	 		
   	 		redirectAttributes.addFlashAttribute("failed", "not owner!!");
   	 		return "redirect:/group/all";
   	 	}
       	model.addAttribute("group", group);
   		request.getSession().setAttribute("groupSession", group);
   		
   		System.out.println("updating group id="+ id);
   		return "/groups/group-update";
   	}
       
       
       @PostMapping("/update")
   	public String updateGroup(@Valid @ModelAttribute Group group,
   			BindingResult results,
   			Model model, 
   			RedirectAttributes redirectAttributes) {
   		
    	   if(group.getGroupOwner().getUserId() != getCurrentUserId()) {
      	 		System.out.println("wrong user!");
      	 		
      	 		redirectAttributes.addFlashAttribute("failed", "not owner!!");
      	 		return "/groups/group-all";
      	 	}	
       else if (results.hasErrors()){
   			
   			return "/groups/group-update";
   		}
          
   		groupService.updateGroup(group);
           redirectAttributes.addFlashAttribute("updated", "group updated!");
   		return "redirect:/group/all";
   		
   	}
       
       @GetMapping("/detail/{id}")
      	public String showDetailGroup(@PathVariable("id") Long id, 
      			Model model,
      			HttpServletRequest request,
      			RedirectAttributes redirectAttributes) {
      	 	Group group = groupService.getGroupById(id); 
          	model.addAttribute("group", group);
      		request.getSession().setAttribute("groupSession", group);
      		
      		System.out.println("detail of group id="+ id);
      		return "/groups/group-detail";
      	}
       
       @PostMapping("/join")
      	public String joinGroup(@Valid @ModelAttribute Group group,
      			BindingResult results,
      			Model model, 
      			RedirectAttributes redirectAttributes) {
      		 
      		groupService.joinGroup(group, getCurrentUserId());
              redirectAttributes.addFlashAttribute("joined", "group joined!");
      		return "redirect:/group/all";
       }
       
       @PostMapping("/leave")
     	public String leaveGroup(@Valid @ModelAttribute Group group,
     			BindingResult results,
     			Model model, 
     			RedirectAttributes redirectAttributes) {
     		 
     		groupService.leaveGroup(group, getCurrentUserId());
             redirectAttributes.addFlashAttribute("left", "group left!");
     		return "redirect:/group/all";
      }
       
}
