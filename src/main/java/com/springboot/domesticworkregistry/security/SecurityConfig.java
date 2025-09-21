package com.springboot.domesticworkregistry.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.springboot.domesticworkregistry.service.employer.EmployerDetailsService;

@Configuration
public class SecurityConfig {

        @Autowired
        private EmployerDetailsService employerDetailsService;

        // @Bean
        // public AuthenticationManager authManager(AuthenticationConfiguration config)
        // throws Exception {
        // return config.getAuthenticationManager();
        // }

        @Bean
        PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider(EmployerDetailsService employerDetailsService) {
                DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
                auth.setUserDetailsService(employerDetailsService);
                auth.setPasswordEncoder(passwordEncoder());
                return auth;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http,
                        AuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {

                http
                                .authorizeHttpRequests(config -> config
                                                .requestMatchers(
                                                                "/css/**", "/js/**", "/images/**", "/",
                                                                "/register/**" // explicitly specify the
                                                                               // form path
                                                )
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .userDetailsService(employerDetailsService)

                                .formLogin(form -> form
                                                .loginPage("/loginPage")
                                                .loginProcessingUrl("/authenticateTheUser")
                                                .successHandler(customAuthenticationSuccessHandler)
                                                .permitAll())
                                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").permitAll())
                                .exceptionHandling(config -> config
                                                .accessDeniedPage("/access-denied"))
                                .authenticationProvider(authenticationProvider(employerDetailsService));

                return http.build();

        }

}
