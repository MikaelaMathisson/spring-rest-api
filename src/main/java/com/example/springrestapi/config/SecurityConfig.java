package com.example.springrestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( auth -> auth
                        .requestMatchers(GET, "/api/categories/**").permitAll()
                        .requestMatchers(POST, "/api/categories/**").hasAuthority("SCOPE_admin")
                        .requestMatchers(GET, "/api/places/user/**").hasAuthority("SCOPE_user")
                        .requestMatchers(GET, "/api/places/**").permitAll()
                        .requestMatchers(GET, "/api/places/category/**").permitAll()
                        .requestMatchers(GET, "/api/places/radius").permitAll()
                        .requestMatchers(POST, "/api/places/**").hasAuthority("SCOPE_user")

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults())
                );
        return http.build();
    }
}
