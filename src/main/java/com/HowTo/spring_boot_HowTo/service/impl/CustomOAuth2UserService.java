package com.HowTo.spring_boot_HowTo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserService userService;

    //load user for oauth
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        //set name = login when the name is not in "name" but in "login"
        if(name == null) {
        	name =  oAuth2User.getAttribute("login");
        }
        //set email if email is private
        if(email == null) {
			email = "placeholdermail" + name + "@how2.com";
        }
        userService.saveO2authUser(email, name);
        return oAuth2User;
    }
}
