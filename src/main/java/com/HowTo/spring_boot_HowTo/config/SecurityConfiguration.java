package com.HowTo.spring_boot_HowTo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.Customizer;

import com.HowTo.spring_boot_HowTo.config.google2fa.CustomAuthenticationProvider;
import com.HowTo.spring_boot_HowTo.config.google2fa.CustomWebAuthenticationDetailsSource;
import com.HowTo.spring_boot_HowTo.service.MyUserDetailService;
import com.HowTo.spring_boot_HowTo.service.impl.CustomOAuth2UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
		
		@Autowired
	    MyUserDetailService userDetailsService;
		@Autowired
		CustomWebAuthenticationDetailsSource authenticationDetailsSource;
		@Autowired
		CustomOAuth2UserService customOAuth2UserService;

		
		//create Encoder for the password 
		@Bean public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		} 
		
		//create the CustomAuthenticationProvider, set our userDetailsService and the passwordEncoder for login
		@Bean public CustomAuthenticationProvider customAuthenticationProvider() {
			CustomAuthenticationProvider provider = new CustomAuthenticationProvider(userDetailsService);
			provider.setPasswordEncoder(passwordEncoder());
			return provider;
		}
		
		
		//set settings of SecurityFilterChain that the different URLs are secure
		@Bean public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
			http
			.csrf().ignoringRequestMatchers(new AntPathRequestMatcher("/api/**")) 
			.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
			.and()
			.headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
			.authenticationProvider(customAuthenticationProvider())
			//set login settings
			.formLogin(formLogin -> formLogin 
					.loginPage("/login") 
					.authenticationDetailsSource(authenticationDetailsSource) 
					.defaultSuccessUrl("/home", true) 
					.failureUrl("/login?error=true") 
					.permitAll()
			)
			//set oauth2 login settings
			.oauth2Login(oauth2 -> oauth2
					.loginPage("/login")
					.authenticationDetailsSource(authenticationDetailsSource) 
					.defaultSuccessUrl("/loginSuccess", true) 
					.failureUrl("/login?error=true") 
			)
			//set logout settings
			.logout(logout -> logout 
			.logoutUrl("/logout") 
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true)
            .clearAuthentication(true)
			.deleteCookies("JSESSIONID")
			.permitAll()
			)
			//set all Pages that are accessible with no authorization
			.authorizeHttpRequests(authorize -> authorize 
			.requestMatchers("/resources/**").permitAll()
			.requestMatchers("/api/**").permitAll()
			.requestMatchers("/api/workshops/**").permitAll()
			.requestMatchers("/webjars/**").permitAll()
			.requestMatchers("/h2-console/**").permitAll()
			.requestMatchers("/register").permitAll()
			.requestMatchers("/registrationConfirm").permitAll()
			.requestMatchers("/").permitAll()
			.requestMatchers("/user/forgotmypassword").permitAll()
			.requestMatchers("/confirmPassword").permitAll()
			.requestMatchers("/user/forgottenpasswordchanged").permitAll()
			.requestMatchers("/v3/api-docs").permitAll()
			.requestMatchers("/swagger-ui/index.html").permitAll()
			.requestMatchers("/swagger").permitAll()
			//set all Pages that are accessible with User authorization
			.requestMatchers("/home").hasAuthority("VIEW")
			.requestMatchers("/tutorial/all").hasAuthority("VIEW")
			.requestMatchers("/tutorial/view/**").hasAuthority("VIEW")
			.requestMatchers("/tutorial/like/**").hasAuthority("VIEW")
			.requestMatchers("/tutorial/dislike/**").hasAuthority("VIEW")
			.requestMatchers("/tutorial/ratings/**").hasAuthority("VIEW")
			.requestMatchers("/channel/view/**").hasAuthority("VIEW")
			.requestMatchers("/channel/subscribe").hasAuthority("VIEW")
			.requestMatchers("/channel/unsubscribe").hasAuthority("VIEW")
			.requestMatchers("/channel/subscriberlist/**").hasAuthority("VIEW")
			.requestMatchers("/channel/subscribed").hasAuthority("VIEW")
			.requestMatchers("/channel/all").hasAuthority("VIEW")
			.requestMatchers("/channel/create").hasAuthority("VIEW")
			.requestMatchers("/group/**").hasAuthority("VIEW")
			.requestMatchers("/user/update/**").hasAuthority("VIEW")
			.requestMatchers("/user/delete").hasAuthority("VIEW")
			.requestMatchers("/user/changemypassword").hasAuthority("VIEW")
			.requestMatchers("/user/passwordchanged").hasAuthority("VIEW")
			.requestMatchers("/history/**").hasAuthority("VIEW")
			.requestMatchers("/comment/**").hasAuthority("VIEW")
			.requestMatchers("/watchLater/**").hasAuthority("VIEW")
			.requestMatchers("/category/view/**").hasAuthority("VIEW")
			.requestMatchers("/user/my/**").hasAuthority("VIEW")
			.requestMatchers("/user/delete").hasAuthority("VIEW")
			.requestMatchers("/chat/**").hasAuthority("VIEW")
			.requestMatchers("/rating/view/**").hasAuthority("VIEW")
			.requestMatchers("/rating/delete/**").hasAuthority("VIEW")
			.requestMatchers("/rating/tutorial/**").hasAuthority("VIEW")
			.requestMatchers("/rating/update/**").hasAuthority("VIEW")
			.requestMatchers("/rating/myrating").hasAuthority("VIEW")
			.requestMatchers("/report/delete/**").hasAuthority("VIEW")
			.requestMatchers("/report/user/**").hasAuthority("VIEW")
			.requestMatchers("/report/myreports").hasAuthority("VIEW")
			.requestMatchers("/wallet/**").hasAuthority("VIEW")
			.requestMatchers("/watchLater/**").hasAuthority("VIEW")
			//set all Pages that are accessible with Creator authorization
			.requestMatchers("/channel/delete/**").hasAuthority("CREATOR_RIGHTS")
			.requestMatchers("/channel/update/**").hasAuthority("CREATOR_RIGHTS")
			.requestMatchers("/tutorial/create").hasAuthority("CREATOR_RIGHTS")
			.requestMatchers("/tutorial/upload").hasAuthority("CREATOR_RIGHTS")
			.requestMatchers("/tutorial/upload/video/**").hasAuthority("CREATOR_RIGHTS")
	        .requestMatchers("/tutorial/uploadvideo/**").hasAuthority("CREATOR_RIGHTS")
	        .requestMatchers("/tutorial/deletevideo/**").hasAuthority("CREATOR_RIGHTS")
			.requestMatchers("/tutorial/update/**").hasAuthority("CREATOR_RIGHTS")
			.requestMatchers("/tutorial/delete/**").hasAuthority("CREATOR_RIGHTS")
			//set all Pages that are accessible with Admin authorization
			.requestMatchers("/admin/**").hasAuthority("ADMIN_RIGHTS")
	        .requestMatchers("/category/create").hasAuthority("ADMIN_RIGHTS")
	        .requestMatchers("/category/all").hasAuthority("ADMIN_RIGHTS")
	        .requestMatchers("/category/audit/**").hasAuthority("ADMIN_RIGHTS")
	        .requestMatchers("/category/delete/**").hasAuthority("ADMIN_RIGHTS")
	        .requestMatchers("/category/update/**").hasAuthority("ADMIN_RIGHTS")
	        .requestMatchers("/category/update").hasAuthority("ADMIN_RIGHTS")
	        .requestMatchers("/advertisement/**").hasAuthority("ADMIN_RIGHTS")
			.requestMatchers("/user/admin/**").hasAuthority("ADMIN_RIGHTS")
			.requestMatchers("/rating/audit/**").hasAuthority("ADMIN_RIGHTS")
			.requestMatchers("/rating/all").hasAuthority("ADMIN_RIGHTS")
			.requestMatchers("/tutorial/audit/**").hasAuthority("ADMIN_RIGHTS")
			.anyRequest().authenticated()
			);
			http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
			http.formLogin(Customizer.withDefaults());
			http.httpBasic(Customizer.withDefaults()); 
			return http.build(); } 

}
