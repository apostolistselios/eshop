package com.eshop.backend.config;

import com.eshop.backend.constants.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.spa())
                .authorizeHttpRequests(auth -> auth
                        // Angular static
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/favicon.ico",
                                "/media/**",
                                "/assets/**",
                                "/*.js",
                                "/*.css"
                        ).permitAll()

                        // Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Csrf endpoint
                        .requestMatchers("/api/csrf").permitAll()

                        // Auth endpoints
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/signup/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // Rest Controllers
                        .requestMatchers("/api/shop/**").hasRole(Roles.SHOP)
                        .requestMatchers("/api/customer/**").hasRole(Roles.CUSTOMER)
                        .requestMatchers("/api/cart/**").hasRole(Roles.CUSTOMER)
                        .requestMatchers("/api/order/**").hasRole(Roles.CUSTOMER)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((req, res, auth) -> res.setStatus(200))
                        .failureHandler((req, res, ex) -> res.setStatus(401))
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler((req, res, auth) -> res.setStatus(200))
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}