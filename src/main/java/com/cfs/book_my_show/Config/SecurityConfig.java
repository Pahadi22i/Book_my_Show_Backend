package com.cfs.book_my_show.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF Disable
                .csrf(csrf -> csrf.disable())

                // 2. CORS Activate
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. Permissions (Yahan galti thi)
                .authorizeHttpRequests(auth -> auth
                        // Sabse pehle: OPTIONS requests ko allow karein (Browser check ke liye)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Specific URLs ko allow karein (Dono tarike se)
                        .requestMatchers("/api/movies", "/api/movies/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // 🔥 TESTING KE LIYE: Filhal sab kuch allow kar do (403 hatane ke liye)
                        // Jab chal jaye, tab is line ko hata kar .authenticated() wapas laga dena
                        .requestMatchers("/**").permitAll())
                .httpBasic(org.springframework.security.config.Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Sabko aane do
        configuration.setAllowedOriginPatterns(List.of("*"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}