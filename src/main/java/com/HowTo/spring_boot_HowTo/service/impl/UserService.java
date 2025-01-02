package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

@Service
public class UserService implements UserServiceI{

	@Autowired
	UserRepositoryI userRepository;
	
	
	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
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

}
