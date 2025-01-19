package com.HowTo.spring_boot_HowTo.config.google2fa;


import java.util.Optional;

import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.repository.impl.UserRepository;

@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserRepository userRepository;
    
    private final UserDetailsService userDetailsService;
    
    //set the UserDetailsService for the CustomAuthenticationProvider
    public CustomAuthenticationProvider(UserDetailsService userDetailsService) { 
    	this.userDetailsService = userDetailsService;
    	super.setUserDetailsService(userDetailsService);
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
    	//get optional User by the Name that wants to be authenticated
    	Optional<User> optionalUser = userRepository.findUserByUsername(auth.getName());
    	System.out.println(optionalUser);
    	//check if user is empty(no User with the Name was in the DB)
    	if (optionalUser.isEmpty()) {
    		throw new BadCredentialsException("Invalid username or password");
    		}
    	//get the Data of the Optional User and put it in a User Object
    	User user = optionalUser.get();
    	//check if User is enabled
    	if(!user.isEnabled()) {
    		throw new BadCredentialsException("user is not enabled. Please verify via E-Mail");
    	}
    	//check if User is using 2FA
        if (user.isUsing2FA()) {
        	//get the 2FA code that the User typed during login
            final String verificationCode = ((CustomWebAuthenticationDetails) auth.getDetails()).getVerificationCode();
            //get the 2FA code from google
            final Totp totp = new Totp(user.getSecret());
            //check if verificationCode that got typed in is correct
            if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
                throw new BadCredentialsException("Invalid verification code");
            }
        }
        System.out.println("Proceeding with super.authenticate()");
        try { 
        	//set authentication
        	final Authentication result = super.authenticate(auth);
        	System.out.println("Super authentication successful");
        	//return UsernamePasswordAuthenticationToken so it can be set in the session
        	return new UsernamePasswordAuthenticationToken(user, result.getCredentials(), result.getAuthorities());
        	} catch (Exception e) {
        		e.printStackTrace();
        		System.out.println("Authentication exception: " + e.getMessage());
        		throw e; // Re-throw the exception to handle it in the controller }
        }
    }

    private boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (final NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}