package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.repository.GroupRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;
import com.HowTo.spring_boot_HowTo.service.GroupServiceI;
@Service
public class GroupService implements GroupServiceI{

	@Autowired
	GroupRepositoryI groupRepository;
	@Autowired
	UserRepositoryI userRepository;
	
	@Override
	public List<Group> getAllGroups() {
		// TODO Auto-generated method stub
		return groupRepository.findAll();
	}
	
	//get all Groups pagination
	public Page<Group> getAllGroups(String name, Pageable pageable) {
		
		Page <Group> pageGroup;
		if (name == null) {
			pageGroup = groupRepository.findAll(pageable);
		 } else {
			 pageGroup = groupRepository.findByNameContainingIgnoreCase(name, pageable);

		 }
		return pageGroup;
	}
	
	//save group and sets the group owner to userid
	@Override
	public Group saveGroup(Group group, Long UserId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(UserId).get();
		List<Group> ownedgroups = user.getOwnedGroups();
		
		if(user != null && group != null && ownedgroups != null) {
			if(!ownedgroups.contains(group)) {
				group.setGroupOwner(user);
				user.addOwnedGroup(group);
				Group r = 
						groupRepository.save(group);
				return r;
			}
		}			
		return group;
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
	
	//adds user to the joined group list
	@Override
	public Group joinGroup(Group group, Long UserId) {
		User user = userRepository.findById(UserId).get();
		List<Group> joinedgroups = user.getJoinedGroups();
		Group realgroup = groupRepository.findById(group.getGroupId()).get();
		
		if(user != null && group != null && joinedgroups != null) {
			if(!joinedgroups.contains(realgroup)) {
				user.addJoinedGroup(realgroup);
				realgroup.addUser(user);
				userRepository.save(user);
			}
		}
		return group;
	}
	
	//removes user from the group
	
	@Override
	public Group leaveGroup(Group group, Long UserId){
		User user = userRepository.findById(UserId).get();
		List<Group> joinedgroups = user.getJoinedGroups();
		Group realgroup = groupRepository.findById(group.getGroupId()).get();
		
		if(user != null && group != null && joinedgroups != null) {
			if(joinedgroups.contains(realgroup)) {
				user.removeJoinedGroup(realgroup);
				realgroup.removeUser(user);
				userRepository.save(user);
			}
		}
		return group;
	}
	
	//deletes group and removes all user
	@Override
	public void delete(Group group) {
		// TODO Auto-generated method stub
		Group realgroup = groupRepository.findById(group.getGroupId()).get();
		List<User> joineduser = realgroup.getUsers();
		for(User e: joineduser) {
			e.removeJoinedGroup(realgroup);
			userRepository.save(e);
		}
		groupRepository.delete(group);
	}
	
}
