package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.HowTo.spring_boot_HowTo.model.User;

public interface UserServiceI {

	Page<User> getAllUsers(String username, Pageable pageable);
	
	List<User> findUserByName(String username);
	
	User saveUser(User user);
	
	User getUserById(Long id);
	
	User updateUser(User user);
	
	void delete(User user);
}
