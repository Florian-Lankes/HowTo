package com.HowTo.spring_boot_HowTo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
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

		
		@Bean public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		} 
		
		@Bean public CustomAuthenticationProvider customAuthenticationProvider() {
			CustomAuthenticationProvider provider = new CustomAuthenticationProvider(userDetailsService);
			provider.setPasswordEncoder(passwordEncoder());
			return provider;
		}
		
		private static final String[] AUTH_WHITE_LIST = {
	            "/v3/api-docs/**",
	            "/swagger-ui/**",
	            "/logout",
	            "/h2-console/**",
	            "/console/**",
	            "/static/**"
	    };
		
		@Bean public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
			http.csrf().disable()
			.csrf().ignoringRequestMatchers(new AntPathRequestMatcher("/api/**")) 
			.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
			.and()
			.headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
			.authenticationProvider(customAuthenticationProvider())
			.formLogin(formLogin -> formLogin 
					.loginPage("/login") 
					.authenticationDetailsSource(authenticationDetailsSource) 
					.defaultSuccessUrl("/home", true) 
					.failureUrl("/login?error=true") 
					.permitAll()
			)
			.oauth2Login(oauth2 -> oauth2
					.loginPage("/login")
					.authenticationDetailsSource(authenticationDetailsSource) 
					.defaultSuccessUrl("/loginSuccess", true) 
					.failureUrl("/login?error=true") 
			)
			.logout(logout -> logout 
			.logoutUrl("/logout") 
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true)
            .clearAuthentication(true)
			.deleteCookies("JSESSIONID")
			.permitAll()
			)
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
			
			.requestMatchers("/home").hasAuthority("VIEW")
			.requestMatchers("/tutorial/all").hasAuthority("VIEW")
			.requestMatchers("/tutorial/view/**").hasAuthority("VIEW")
			.requestMatchers("/tutorial/like/**").hasAuthority("VIEW")
			.requestMatchers("/channel/view/**").hasAuthority("VIEW")
			.requestMatchers("/channel/subscribe").hasAuthority("VIEW")
			.requestMatchers("/channel/unsubscribe").hasAuthority("VIEW")
			.requestMatchers("/channel/subscriberlist/**").hasAuthority("VIEW")
			.requestMatchers("/channel/subscribed").hasAuthority("VIEW")
			.requestMatchers("/channel/all").hasAuthority("VIEW")
			.requestMatchers("/channel/create").hasAuthority("VIEW")
			.requestMatchers("/group/**").hasAuthority("VIEW")
			.requestMatchers("/user/update/**").hasAuthority("VIEW")
			.requestMatchers("/history/**").hasAuthority("VIEW")
			.requestMatchers("/comment/**").hasAuthority("VIEW")
			.requestMatchers("/watchLater/**").hasAuthority("VIEW")
			.requestMatchers("/category/view/**").hasAuthority("VIEW")
	        .requestMatchers("/category/all").hasAuthority("VIEW")
			.requestMatchers("/user/my/**").hasAuthority("VIEW")
			.requestMatchers("/chat/**").hasAuthority("VIEW")
			
			.requestMatchers("/channel/delete/**").hasAuthority("CREATOR_RIGHTS")
			.requestMatchers("/channel/update/**").hasAuthority("CREATOR_RIGHTS")
			.requestMatchers("/tutorial/create").hasAuthority("CREATOR_RIGHTS")
			.requestMatchers("/tutorial/upload").hasAuthority("CREATOR_RIGHTS")
	        .requestMatchers("/tutorial/uploadvideo/**").hasAuthority("CREATOR_RIGHTS")
	        .requestMatchers("/tutorial/deletevideo/**").hasAuthority("CREATOR_RIGHTS")
			.requestMatchers("/tutorial/update/**").hasAuthority("CREATOR_RIGHTS")
			.requestMatchers("/tutorial/delete/**").hasAuthority("CREATOR_RIGHTS")
			
			.requestMatchers("/admin/**").hasAuthority("ADMIN_RIGHTS")
	        .requestMatchers("/category/create").hasAuthority("ADMIN_RIGHTS")
	        .requestMatchers("/category/delete/**").hasAuthority("ADMIN_RIGHTS")
	        .requestMatchers("/category/update/**").hasAuthority("ADMIN_RIGHTS")
	        .requestMatchers("/category/update").hasAuthority("ADMIN_RIGHTS")
	        .requestMatchers("/advertisement/**").hasAuthority("ADMIN_RIGHTS")
			.requestMatchers("/user/admin/**").hasAuthority("ADMIN_RIGHTS")
			.anyRequest().authenticated()
			);
			http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
			http.formLogin().defaultSuccessUrl("/home", true);
			http.formLogin(Customizer.withDefaults());
			http.httpBasic(Customizer.withDefaults()); 
			return http.build(); } 

	    @Bean
	    public WebSecurityCustomizer webSecurityCustomizer() {
	        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**", "/css/**");
	    }
	    
	    @Bean
        public AuthenticationManager authenticationManager(
        		AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }

	
//		@Bean
//		public PasswordEncoder getPasswordEncoder() {
//			//return new BCryptPasswordEncoder();
//			return new BCryptPasswordEncoder();
//		}

}
