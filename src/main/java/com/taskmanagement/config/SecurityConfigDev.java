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
                .anyRequest().permitAll()   // Allow all requests
            )
            .csrf(csrf -> csrf.disable())  // Disable CSRF for simplicity
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin()) // H2 console support
            );

        return http.build();
    }
}
