package de.HowTo.service;

import java.util.List;

import de.HowTo.model.User;


public interface UserService {

	List<User> getAllUsers();
	
	User saveUser(User user);
	
	User getuserById(Long id);
	
	User updateUser(User user);
	
	void delete(User user);
	
}

