package com.springboot.domesticworkregistry.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.springboot.domesticworkregistry.service.user.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity // 👈 allows @PreAuthorize annotations too
public class SecurityConfig {

        private final CustomUserDetailsService customUserDetailsService;
        private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

        public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                        CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
                this.customUserDetailsService = customUserDetailsService;
                this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;

        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
                auth.setUserDetailsService(customUserDetailsService);
                auth.setPasswordEncoder(passwordEncoder());
                return auth;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http

                                .authorizeHttpRequests(config -> config
                                                // public
                                                .requestMatchers("/css/**", "/js/**", "/images/**", "/", "/register/**",
                                                                "/email/**", "/data/**", "/help/**")
                                                .permitAll()

                                                // employer-only
                                                .requestMatchers("/employers/**", "/contract/**").hasRole("EMPLOYER")

                                                // employee-only
                                                .requestMatchers("/employees/**").hasRole("EMPLOYEE")

                                                // shared
                                                .requestMatchers("/dashboard/**", "/attendance/**")
                                                .hasAnyRole("EMPLOYER", "EMPLOYEE")

                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/loginPage")
                                                .loginProcessingUrl("/authenticateTheUser")
                                                .successHandler(customAuthenticationSuccessHandler) // 👈 handles
                                                                                                    // redirect logic
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .permitAll())
                                .exceptionHandling(config -> config
                                                .accessDeniedPage("/access-denied"))
                                .authenticationProvider(authenticationProvider());

                return http.build();
        }
}
