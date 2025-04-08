package com.springboot.domesticworkregistry.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import com.springboot.domesticworkregistry.service.employer.EmployerDetailsService;

@Configuration
public class SecurityConfig {

        @Autowired
        private EmployerDetailsService employerDetailsService;


        @Bean
        public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                .authorizeHttpRequests(config -> config
                                                .requestMatchers("/css/**", "/js/**", "/images/**", "/").permitAll()
                                                .anyRequest().authenticated())
                                .userDetailsService(employerDetailsService)

                                .formLogin(form -> form
                                                .loginPage("/loginPage")
                                                .loginProcessingUrl("/authenticateTheUser")
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout(logout -> logout.permitAll())
                                .exceptionHandling(config -> config
                                                .accessDeniedPage("/access-denied"));

                return http.build();

        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
}
