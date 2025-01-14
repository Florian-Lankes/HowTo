package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.HowTo.spring_boot_HowTo.model.Group;


public interface GroupServiceI {
	
	List<Group> getAllGroups();
	
	Page<Group> getAllGroups(String name, Pageable pageable);
	
	Group saveGroup(Group group, Long userId);
	
	Group getGroupById(Long id);
	
	Group updateGroup(Group group);
	
	Group joinGroup(Group group, Long userId);
	
	Group leaveGroup(Group group, Long userId);
	
	void delete(Group group);
}
