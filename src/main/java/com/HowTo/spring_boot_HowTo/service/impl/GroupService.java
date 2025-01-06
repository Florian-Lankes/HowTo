package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.repository.GroupRepositoryI;

import com.HowTo.spring_boot_HowTo.service.GroupServiceI;
@Service
public class GroupService implements GroupServiceI{

	@Autowired
	GroupRepositoryI groupRepository;
	
	@Override
	public List<Group> getAllGroups() {
		// TODO Auto-generated method stub
		return groupRepository.findAll();
	}

	@Override
	public Group saveGroup(Group group) {
		// TODO Auto-generated method stub
		return groupRepository.save(group);
	}

	@Override
	public Group getGroupById(Long id) { 
		// TODO Auto-generated method stub
		Optional<Group> opGroup = groupRepository.findById(id);
		return opGroup.isPresent()? opGroup.get():null;
	}

	@Override
	public Group updateGroup(Group group) {
		Group local = groupRepository.save(group);
		return local;
	}

	@Override
	public void delete(Group group) {
		// TODO Auto-generated method stub
		groupRepository.delete(group);
	}
	
}
