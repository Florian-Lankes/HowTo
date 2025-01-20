package com.HowTo.spring_boot_HowTo.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.HowTo.spring_boot_HowTo.model.Authority;
import com.HowTo.spring_boot_HowTo.model.Role;
import com.HowTo.spring_boot_HowTo.model.User;

public class MyUserDetails implements UserDetails  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName;
	private String password;
	private Long id;
	private boolean active;
	private boolean isUsing2FA;
    private String secret;
    private List<GrantedAuthority> authorities;
	private List<Role> roles;
	
	public MyUserDetails(User user) {
		// set data of the user
		this.userName = user.getUsername();
		this.password = user.getPassword();
		this.id = user.getUserId();
		System.out.println("password of the user is=" + password);
		System.out.println("userName of the user is=" + this.userName);
		System.out.println("id of the user is=" + this.id);
		this.active = user.isActive();
		this.setSecret(user.getSecret());
		this.setUsing2FA(user.isUsing2FA());

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
//
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

	public Long getId() {
		return this.id;
	}
	public boolean isUsing2FA() {
		return isUsing2FA;
	}
	public void setUsing2FA(boolean isUsing2FA) {
		this.isUsing2FA = isUsing2FA;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
}
