package com.example.ktech_project_3.config;

import com.example.ktech_project_3.jwt.JwtTokenFilter;
import com.example.ktech_project_3.jwt.JwtTokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
@Configuration
public class WebSecurityConfig {
    private final JwtTokenUtils tokenUtils;
    private final UserDetailsService detailsService;

    public WebSecurityConfig(
            JwtTokenUtils tokenUtils,
            UserDetailsService detailsService
    ) {
        this.tokenUtils = tokenUtils;
        this.detailsService = detailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/users/login")
                            .permitAll();
                    auth.requestMatchers("/users/register")
                            .anonymous();
//                    auth.requestMatchers("/users/my-profile")
//                            .authenticated();
                    auth.requestMatchers("/users/*/update-profile",
                                    "/users/{username}/update-image")
                            .authenticated();
//                    auth.requestMatchers("/user-role")
//                            .hasRole("USER");
//                    auth.requestMatchers("/admin-role")
//                            .hasRole("ADMIN");
                })
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )

                .addFilterBefore(
                        new JwtTokenFilter(
                                tokenUtils,
                                detailsService
                        ),
                        AuthorizationFilter.class
                );
        return http.build();

    }
}
