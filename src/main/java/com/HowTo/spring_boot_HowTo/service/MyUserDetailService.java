package com.HowTo.spring_boot_HowTo.service;


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
		// get User, happens after successful login
		 try {
	            User user = userRepository.findUserByUsername(username).get();
	            if (user == null) {
	                throw new UsernameNotFoundException("No user found with username: " + username);
	            }

	            return new MyUserDetails(user);
	          
	        } catch (final Exception e) {
	            throw new RuntimeException(e);
	        }
	}

}
