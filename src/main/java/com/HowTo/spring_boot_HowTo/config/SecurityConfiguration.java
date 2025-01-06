package com.HowTo.spring_boot_HowTo.config;


import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.Customizer;

import com.HowTo.spring_boot_HowTo.service.MyUserDetailService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
		
		@Autowired
	    MyUserDetailService userDetailsService;
		
		private static final String[] AUTH_WHITE_LIST = {
	            "/v3/api-docs/**",
	            "/swagger-ui/**",
	            "/logout",
	            "/h2-console/**",
	            "/console/**"
	            
	    };
		
			    
	    @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    	
	    	http.csrf().disable();
	        http.csrf()
	        .ignoringRequestMatchers(new AntPathRequestMatcher("/api/**"))
	        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"));
	        
	        http.headers(headersConfigurer ->
	        headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

	        
	      //sites that don't need Authority
	    	http.authorizeHttpRequests(auth ->
            auth
            		.requestMatchers(new AntPathRequestMatcher("/resources/**")).permitAll()	
            		.requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
            		.requestMatchers(new AntPathRequestMatcher("/api/workshops/**")).permitAll()
            		
            		.requestMatchers(new AntPathRequestMatcher("/webjars/**")).permitAll()
            		.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
            		.requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
            		.requestMatchers(new AntPathRequestMatcher("/register")).permitAll()
            		.requestMatchers(new AntPathRequestMatcher("/logout")).permitAll()
            		.requestMatchers(new AntPathRequestMatcher("/")).permitAll());
	    	
	    	http.formLogin().defaultSuccessUrl("/home",true);
    
	    	
	    	//sites that need User Authority
	    	http.authorizeHttpRequests()
		     
	        .requestMatchers(new AntPathRequestMatcher("/home")).hasAuthority("VIEW")
    		.requestMatchers(new AntPathRequestMatcher("/tutorial/all")).hasAuthority("VIEW")
    		.requestMatchers(new AntPathRequestMatcher("/tutorial/view/**")).hasAuthority("VIEW")
    		.requestMatchers(new AntPathRequestMatcher("/tutorial/like/**")).hasAuthority("VIEW")
    		.requestMatchers(new AntPathRequestMatcher("/channel/view/**")).hasAuthority("VIEW")
    		.requestMatchers(new AntPathRequestMatcher("/channel/all")).hasAuthority("VIEW")
    		.requestMatchers(new AntPathRequestMatcher("/channel/create")).hasAuthority("VIEW")
    		.requestMatchers(new AntPathRequestMatcher("/user/update/**")).hasAuthority("VIEW")
	    	.requestMatchers(new AntPathRequestMatcher("/history/**")).hasAuthority("VIEW");

	    	//sites that need Creator Authority
	    	http.authorizeHttpRequests()
	     
	        .requestMatchers(new AntPathRequestMatcher("/channel/delete/**")).hasAuthority("CREATOR_RIGHTS")
	        .requestMatchers(new AntPathRequestMatcher("/channel/update/**")).hasAuthority("CREATOR_RIGHTS")
	        .requestMatchers(new AntPathRequestMatcher("/tutorial/create")).hasAuthority("CREATOR_RIGHTS")
	        .requestMatchers(new AntPathRequestMatcher("/tutorial/upload")).hasAuthority("CREATOR_RIGHTS")
	        .requestMatchers(new AntPathRequestMatcher("/tutorial/update/**")).hasAuthority("CREATOR_RIGHTS")
	        .requestMatchers(new AntPathRequestMatcher("/tutorial/delete/**")).hasAuthority("CREATOR_RIGHTS");
	    
	    	//sites that need Authority
	    	http.authorizeHttpRequests()
	     
	        .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAuthority("ADMIN_RIGHTS")
	        .requestMatchers(new AntPathRequestMatcher("/user/admin/**")).hasAuthority("ADMIN_RIGHTS");

	    	
	    	http.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable));   
	    	
	    	http.formLogin(Customizer.withDefaults());
	        http.httpBasic(Customizer.withDefaults());
	        
	    	
	        return http.build();
	    }
	 
	    @Bean
	    public WebSecurityCustomizer webSecurityCustomizer() {
	        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
	    }
	    
	    @Bean
        public AuthenticationManager authenticationManager(
        		AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }
	    
	
	    @Bean
		public PasswordEncoder getPasswordEncoder() {
			//return new BCryptPasswordEncoder();
			return NoOpPasswordEncoder.getInstance();
		}

}
