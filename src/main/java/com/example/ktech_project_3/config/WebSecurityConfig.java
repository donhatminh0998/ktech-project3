package com.example.ktech_project_3.config;

import com.example.ktech_project_3.jwt.JwtTokenFilter;
import com.example.ktech_project_3.jwt.JwtTokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
@EnableWebSecurity
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
                    auth.requestMatchers("login",
                                    "static/**",
                                    "/itemCategory")
                            .permitAll();
                    auth.requestMatchers("/register")
                            .anonymous();
                    auth.requestMatchers(
                            /*"/users/update-profile",
                                    "/users/{username}/update-image")
                            */
                            "/users/**")
                            .authenticated();
                    auth.requestMatchers("/openRequest").hasRole("USER");
                    auth.requestMatchers("openRequest/confirm/{openRequestId}").hasRole("ADMIN");
                    auth.requestMatchers("/admin/**").hasRole("ADMIN");
                    auth.requestMatchers("/orders/**").hasAuthority("ORDER");
                    auth.requestMatchers("/shop/**").hasRole("BUSINESS");
                    auth.requestMatchers("/read/**").hasAuthority("READ.REQUEST");
                    auth.requestMatchers("/view/**").hasAuthority("VIEW");


//                    auth.requestMatchers("/user-role")
//                            .hasRole("USER");
//                    auth.requestMatchers("/admin-role")
//                            .hasRole("ADMIN");
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

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
