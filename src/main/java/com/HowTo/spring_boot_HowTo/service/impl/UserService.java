package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.RoleRepositoryI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

@Service
public class UserService implements UserServiceI{

	@Autowired
	UserRepositoryI userRepository;
	@Autowired
	RoleRepositoryI roleRepository;
	
//	@Override
//	public List<User> getAllUsers() {
//		// TODO Auto-generated method stub
//		return userRepository.findAll();
//	}
		
	@Override
	public Page<User> getAllUsers(String username, Pageable pageable) {
		// TODO Auto-generated method stub
		Page <User> pageUsers;
		if (username == null) {
			pageUsers = userRepository.findAll(pageable);
		 } else {
			 pageUsers = userRepository.findByUsernameContainingIgnoreCase(username, pageable);
	
		 }
		return pageUsers;
	}

	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		user.setRoles(Collections.singletonList(roleRepository.findByDescription("USER")));
		return userRepository.save(user);
	}

	@Override
	public User getUserById(Long id) {
		// TODO Auto-generated method stub
		Optional<User> opUser = userRepository.findById(id);
		return opUser.isPresent()? opUser.get(): null;
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		User local = userRepository.save(user);
		return local;
	}

	@Override
	public void delete(User user) {
		// TODO Auto-generated method stub
		userRepository.delete(user);
		
	}

	@Override
	public List<User> findUserByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
