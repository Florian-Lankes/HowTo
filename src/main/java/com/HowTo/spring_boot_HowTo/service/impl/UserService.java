package com.HowTo.spring_boot_HowTo.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	
	private final PasswordEncoder passwordEncoder;
	
	
	public static String QR_PREFIX = "https://qrcode.tec-it.com/API/QRCode?data=";
    public static String APP_NAME = "SpringRegistration";
	
	public UserService() {
		this.passwordEncoder = new BCryptPasswordEncoder();
	};
	
	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}
	
		
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
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Collections.singletonList(roleRepository.findByDescription("USER")));
		return userRepository.save(user);
	}
	

	@Override
	public User saveO2authUser(String email, String name) {
		// TODO Auto-generated method stub
		User user = new User();
		  if (usernameExists(name) & emailExists(email)) {
			  User u = userRepository.findUserByUsername(name).get();
			  User w = userRepository.findUserByEmail(email).get();
			  String userEmail1 = u.getEmail();
			  String userEmail2 = w.getEmail();
			  if (userEmail1 == userEmail2){
				  System.out.println("test");
				  return w;
			  }else {
				  throw new UserAlreadyExistException("There is an account with that username: "
			              + user.getUsername());
			  }
	        }
		  if (!emailExists(user.getEmail())) {
				user.setEmail(email);
				user.setUsername(name);
				user.setEnabled(true);
				user.setUsingOauth(true);
				user.setPassword(name);
				user.setBirthDate(LocalDate.of(2020, 1, 20));
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				user.setRoles(Collections.singletonList(roleRepository.findByDescription("USER")));
				userRepository.save(user);
				return user;
		  }else {
			  throw new UserAlreadyExistException("There is an account with that username: "
		              + user.getUsername());
		  }
//		user.setEmail(email);
//		user.setUsername(name);
//		user.setEnabled(true);
//		user.setPassword(name);
//		user.setBirthDate(LocalDate.of(2020, 1, 20));
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		user.setRoles(Collections.singletonList(roleRepository.findByDescription("USER")));
//		userRepository.save(user);
//		return user;
	}
	
	
  @Override
    public VerificationToken getVerificationToken( String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }
  
  @Override
  public void createVerificationTokenForUser(User user, String token) {
      VerificationToken myToken = new VerificationToken();
      myToken.setUser(user);
      myToken.setToken(token);
      myToken.setExpiryDate();
      User u = userRepository.findById(user.getUserId()).get();
      u.setVerificationToken(myToken);
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
		User u = userRepository.findById(user.getUserId()).get();
		//u.setPassword(passwordEncoder.encode(user.getPassword())); double hashes the password if not changed
		u.setUsername(user.getUsername());
		u.setBirthDate(user.getBirthDate());
		u.setEmail(user.getEmail());
		User local = userRepository.save(u);
		return local;
	}
	
	@Override
	public User changePassword(User user) {
		User u = userRepository.findById(user.getUserId()).get();
		u.setPassword(passwordEncoder.encode(user.getPassword()));
		User local = userRepository.save(u);
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
	
	public boolean checkAdmin(User user) {
		// TODO Auto-generated method stub
		return user.getRoles().contains(roleRepository.findByDescription("ADMIN"));
	}


	private boolean emailExists(String email) {
		return !userRepository.findUserByEmail(email).isEmpty();
	}
	
	private boolean usernameExists(String username) {
		return !userRepository.findUserByUsername(username).isEmpty();
	}
	
	@Override
    public String generateQRUrl(User user) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, user.getEmail(), user.getSecret(), APP_NAME), "UTF-8");
    }
	
	@Override
	public Optional<User> getUserByEmail(String email) {
		Optional<User> u = userRepository.findUserByEmail(email);
		return u;
	}

}
