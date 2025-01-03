package com.HowTo.spring_boot_HowTo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.config.MyUserDetails;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;

@Service
public class MyUserDetailService implements UserDetailsService{

	
	@Autowired
	UserRepositoryI userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> oUser= userRepository.findUserByUsername(username);
		oUser.orElseThrow(()-> new UsernameNotFoundException("Not found "+username));
		System.out.println("User found at the UserDetailsService="+ oUser.get().getUsername());
		return new MyUserDetails(oUser.get());
	}

	
	
}
