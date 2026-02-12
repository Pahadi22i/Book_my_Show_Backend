package com.cfs.book_my_show.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter; // Import Zaroori hai

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // Note: Humne yahan .cors() hata diya hai kyunki hum niche alag bean use kar
                // rahe hain

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Pre-flight checks
                        .requestMatchers("/api/movies", "/api/movies/**").permitAll() // Movies Public
                        .requestMatchers("/api/auth/**").permitAll() // Auth Public
                        .anyRequest().authenticated() // Baaki sab secured
                )
                .httpBasic(org.springframework.security.config.Customizer.withDefaults());

        return http.build();
    }

    // 🔥 BRAHMASTRA FIX: Security se alag CORS Filter
    // Ye method Security se pehle run hota hai aur Headers inject kar deta hai
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("*")); // Sab kuch allow
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}