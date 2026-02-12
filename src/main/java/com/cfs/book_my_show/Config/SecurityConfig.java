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

import java.util.Arrays; // Arrays import karein
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF Disable
                .csrf(csrf -> csrf.disable())

                // 2. CORS Activate (Sabse pehle ye run hoga)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. Permissions
                .authorizeHttpRequests(auth -> auth
                        // OPTIONS requests ko allow karein (Preflight check ke liye zaroori hai)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public Endpoints
                        .requestMatchers(HttpMethod.GET, "/api/movies/**").permitAll() // Movies fetch karna free hai
                        .requestMatchers("/api/auth/**").permitAll()

                        // Baaki sab Secured
                        .anyRequest().authenticated())
                .httpBasic(org.springframework.security.config.Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ FIX: Dono versions add karein (Slash ke sath aur bina Slash ke)
        configuration.setAllowedOrigins(Arrays.asList(
                "https://itsmovietime.vercel.app", // Bina slash
                "https://itsmovietime.vercel.app/", // Slash ke sath (Zaroori hai)
                "http://localhost:5173" // Local testing
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}