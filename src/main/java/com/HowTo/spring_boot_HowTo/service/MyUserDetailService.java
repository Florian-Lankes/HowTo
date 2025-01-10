package com.HowTo.spring_boot_HowTo.service;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.config.MyUserDetails;
import com.HowTo.spring_boot_HowTo.model.Authority;
import com.HowTo.spring_boot_HowTo.model.Role;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;

@Service
public class MyUserDetailService implements UserDetailsService{

	
	@Autowired
	UserRepositoryI userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		 try {
	            User user = userRepository.findUserByUsername(username).get();
	            if (user == null) {
	                throw new UsernameNotFoundException("No user found with username: " + username);
	            }

	            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true,getAuthorities(user.getRoles()));
	        } catch (final Exception e) {
	            throw new RuntimeException(e);
	        }
//		Optional<User> oUser= userRepository.findUserByUsername(username);
//		oUser.orElseThrow(()-> new UsernameNotFoundException("Not found "+username));
//		System.out.println("User found at the UserDetailsService="+ oUser.get().getUsername());
//		return new MyUserDetails(oUser.get());
	}

	 private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
	        return getGrantedAuthorities(getPrivileges(roles));
	    }
	
	 private List<String> getPrivileges(final Collection<Role> roles) {
	        final List<String> privileges = new ArrayList<>();
	        final List<Authority> collection = new ArrayList<>();
	        for (final Role role : roles) {
	            privileges.add(role.getDescription());
	            collection.addAll(role.getAuthorities());
	        }
	        for (final Authority item : collection) {
	            privileges.add(item.getDescription());
	        }

	        return privileges;
	    }
	 
	 private List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
	        final List<GrantedAuthority> authorities = new ArrayList<>();
	        for (final String privilege : privileges) {
	            authorities.add(new SimpleGrantedAuthority(privilege));
	        }
	        return authorities;
	    }

}
