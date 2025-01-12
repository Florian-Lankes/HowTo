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
    
    @Autowired public CustomAuthenticationProvider(UserDetailsService userDetailsService) { 
    	this.userDetailsService = userDetailsService;
    	super.setUserDetailsService(userDetailsService);
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
    	Optional<User> optionalUser = userRepository.findUserByUsername(auth.getName());
    	System.out.println(optionalUser);
    	System.out.println("test1.1");
    	if (optionalUser.isEmpty()) {
    		System.out.println("test1.2");
    		throw new BadCredentialsException("Invalid username or password");
    		}
    	User user = optionalUser.get();
    	System.out.println("test1.3");
        // to verify verification code
        if (user.isUsing2FA()) {
            final String verificationCode = ((CustomWebAuthenticationDetails) auth.getDetails()).getVerificationCode();
            final Totp totp = new Totp(user.getSecret());
            if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
                throw new BadCredentialsException("Invalid verification code");
            }

        }
        System.out.println("test1.4");
        System.out.println("Proceeding with super.authenticate()");
        try { 
        	final Authentication result = super.authenticate(auth);
        	System.out.println("Super authentication successful");
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