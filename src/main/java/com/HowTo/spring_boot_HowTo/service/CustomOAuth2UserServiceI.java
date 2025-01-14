package com.HowTo.spring_boot_HowTo.service;


import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;


public interface CustomOAuth2UserServiceI {
	
	OAuth2User loadUser(OAuth2UserRequest userRequest);
}
