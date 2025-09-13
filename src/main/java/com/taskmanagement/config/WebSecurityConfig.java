package com.taskmanagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests((requests) -> requests
	            .requestMatchers("/", "/home").permitAll()
	            .anyRequest().authenticated()
	        )
	        .formLogin((form) -> form
	            .loginPage("/login")
	            .defaultSuccessUrl("/hello")
	            .permitAll()
	        )
	        .oauth2Login((oauth2) -> oauth2
	                .loginPage("/login")
	                .defaultSuccessUrl("/hello")
	            )
	        .logout((logout) -> logout.permitAll());
	    
	    return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() {
	    UserDetails user = User.builder()
	        .username("user")
	        .password(passwordEncoder().encode("password"))
	        .roles("USER")
	        .build();
	    
	    return new InMemoryUserDetailsManager(user);
	}
}
