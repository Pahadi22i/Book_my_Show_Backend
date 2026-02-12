package com.cfs.book_my_show.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        // CORS ko Spring Security ke filter chain mein sabse upar rakhna zaroori hai
        .cors(Customizer.withDefaults()) 
        .authorizeHttpRequests(auth -> auth
            // Dono patterns allow karein: /api/movies aur /api/movies/123
            .requestMatchers("/api/movies", "/api/movies/**").permitAll()
            .requestMatchers("/api/booking/**", "/api/shows/**", "/api/theaters/**").permitAll()
            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults());

    return http.build();
}

    // 🔥 YE SABSE ZAROORI HAI (CORS FIX)
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Frontend ka URL allow karein
        config.setAllowedOrigins(List.of(
                "https://itsmovietime.vercel.app" // Live URL
                // "http://localhost:5173" // Local React URL
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}