package com.HowTo.spring_boot_HowTo.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.HowTo.spring_boot_HowTo.model.Authority;
import com.HowTo.spring_boot_HowTo.model.Role;
import com.HowTo.spring_boot_HowTo.model.User;

public class MyUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String userName;
	private String password;
	private Long id;
	private boolean active;
	private List<GrantedAuthority> authorities;
	private List<Role> roles;

	public MyUserDetails(User user) {
		// TODO Auto-generated constructor stub
		
		this.userName = user.getUsername();
		this.password = user.getPassword();
		this.id = user.getUserId();
		System.out.println("password of the user is=" + password);
		System.out.println("userName of the user is=" + this.userName);
		System.out.println("id of the user is=" + this.id);
		this.active = user.isActive();

		List<Role> myRoles = (List<Role>) user.getRoles();

		System.out.println("the user " + user.getUsername() + " has " + myRoles.size() + " roles");
		this.roles = myRoles;
		authorities = new ArrayList<>();

		// passing the authorities of each Profile from the DB to the Spring Security
		// collection UserDetails.authorities
		for (int i = 0; i < myRoles.size(); i++) {
			List<Authority> myAuthsProfile = (List<Authority>) myRoles.get(i).getAuthorities();
			for (Authority auth : myAuthsProfile) {
				authorities.add(new SimpleGrantedAuthority(auth.getDescription().toUpperCase()));
				System.out.println("the authority" + i + " of the profile " + myRoles.get(i).getDescription()
						+ " of the user " + user.getUsername() + " is " + auth.getDescription());
			}
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.active;
	}

	public Long getId() {
		return this.id;
	}
	
}
