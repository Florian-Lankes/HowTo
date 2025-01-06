package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.Group;


public interface GroupServiceI {
	
	List<Group> getAllGroups();
	
	Group saveGroup(Group group);
	
	Group getGroupById(Long id);
	
	Group updateGroup(Group group);
	
	void delete(Group group);
}
