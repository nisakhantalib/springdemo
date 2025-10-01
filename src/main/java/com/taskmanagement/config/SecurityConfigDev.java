package com.taskmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("dev")  // Only active when 'spring.profiles.active=dev'
public class SecurityConfigDev {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                 // ✅ allow access to /users without login
                .requestMatchers("/users", "/tasks", "/users/**", "/tasks/**").permitAll()
                // also allow static resources like css/js/images
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                // everything else requires login
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")  // your custom login page
                .permitAll()
            )
            .logout(logout -> logout.permitAll());


        return http.build();
    }
}
