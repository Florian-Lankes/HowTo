package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Comment;
import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.VerificationToken;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.VerificationTokenRepository;
import com.HowTo.spring_boot_HowTo.repository.CommentRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.GroupRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.RoleRepositoryI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;
import com.HowTo.spring_boot_HowTo.validator.UserAlreadyExistException;

@Service
public class UserService implements UserServiceI {

	@Autowired
	UserRepositoryI userRepository;
	@Autowired
	RoleRepositoryI roleRepository;
	@Autowired
    VerificationTokenRepository tokenRepository;
	CommentRepositoryI commentRepository;
	@Autowired
	GroupRepositoryI groupRepository;

	
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
		  if (emailExists(user.getEmail())) {
	            throw new UserAlreadyExistException("There is an account with that email address: "
	              + user.getEmail());
	        }
		  if (usernameExists(user.getUsername())) {
	            throw new UserAlreadyExistException("There is an account with that username: "
	              + user.getUsername());
	        }
		user.setRoles(Collections.singletonList(roleRepository.findByDescription("USER")));
		return userRepository.save(user);
	}
	
  @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }
  
  @Override
  public void createVerificationTokenForUser(final User user, final String token) {
      VerificationToken myToken = new VerificationToken();
      myToken.setUser(user);
      myToken.setToken(token);
      myToken.setExpiryDate();
      tokenRepository.save(myToken);
  }
	
    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

	@Override
	public User getUserById(Long id) {
		// TODO Auto-generated method stub
		Optional<User> opUser = userRepository.findById(id);
		return opUser.isPresent()? opUser.get(): null;
	}
	
	 @Override
	    public User getUserByToken(String verificationToken) {
	        User user = tokenRepository.findByToken(verificationToken).getUser();
	        return user;
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
		// TODO Delete groups
		List<Group> allGroups = user.getOwnedGroups();
		allGroups.forEach(group -> groupRepository.delete(group));
		// TODO Delete Comments
		List<Comment> allComments = user.getOwnedComments();
		allComments.forEach(comment -> commentRepository.delete(comment));
		userRepository.delete(user);
	}

	@Override
	public List<User> findUserByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean emailExists(String email) {
		return !userRepository.findUserByEmail(email).isEmpty();
	}
	
	private boolean usernameExists(String username) {
		return !userRepository.findUserByUsername(username).isEmpty();
	}

}
