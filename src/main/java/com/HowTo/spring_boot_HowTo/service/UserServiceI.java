package com.HowTo.spring_boot_HowTo.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.VerificationToken;

public interface UserServiceI {

	Page<User> getAllUsers(String username, Pageable pageable);
	
	List<User> findUserByName(String username);
	
	User saveUser(User user);
	
	User getUserById(Long id);
	
	User getUserByToken(String verificationToken);
	
	User updateUser(User user);
	
	void delete(User user);
	
	void saveRegisteredUser(User user);
	
	VerificationToken getVerificationToken(String VerificationToken);

	void createVerificationTokenForUser(User user, String token);
	
	String generateQRUrl(User user) throws UnsupportedEncodingException;

	User saveO2authUser(String email, String name);

}
