package com.chat.ChatKarlore.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration              // ye batata ha security configuration wali file ha
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean                       // spring security ke rule banata ha
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf->csrf.disable())//  restapi ke liye post request pe csrf disable hoga
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))        // jwt me server session store nhi karega har request apna token leke ayei
                .authorizeHttpRequests(auth-> auth.requestMatchers("/api/auth/signup","/api/auth/login","/chat/**","/chat").permitAll().anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
