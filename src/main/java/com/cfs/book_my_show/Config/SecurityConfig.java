package com.cfs.book_my_show.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // 1. CSRF Disable: Iske bina POST, PUT, DELETE requests block ho jati hain
                                .csrf(csrf -> csrf.disable())

                                // 2. CORS Config: Niche wala bean use karega
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                                // 3. Authorization Rules
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                // Movies APIs (Get, Add, Delete sab allowed)
                                                                "/api/movies",
                                                                "/api/movies/**",

                                                                // Users APIs (Login, Signup, Get Profile allowed)
                                                                "/api/users",
                                                                "/api/users/**",

                                                                // Bookings APIs (Book ticket, Get history allowed)
                                                                "/api/bookings",
                                                                "/api/bookings/**",

                                                                // Shows APIs (Create show, Get seats allowed)
                                                                "/api/shows",
                                                                "/api/shows/**",

                                                                // Theaters APIs
                                                                "/api/theaters",
                                                                "/api/theaters/**")
                                                .permitAll() // 🔥 Iska matlab: "Bina Password ke aane do"

                                                // Baki koi aur URL ho to login maang lena (Safety ke liye)
                                                .anyRequest().authenticated())
                                .httpBasic(Customizer.withDefaults());

                return http.build();
        }

        // 🔥 CORS CONFIGURATION (Frontend Connection ke liye)
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();

                // 1. Allowed Origins (Frontend URLs)
                config.setAllowedOrigins(Arrays.asList(
                                "https://itsmovietime.vercel.app", // Live React App
                                "http://localhost:5173", // Local React App
                                "http://localhost:3000" // Kabhi kabhi React 3000 par chalta hai
                ));

                // 2. Allowed Methods (Ye line ensure karti hai ki DELETE/PUT/POST sab chalein)
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

                // 3. Allowed Headers
                config.setAllowedHeaders(
                                Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));

                // 4. Cookies Allow
                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
        }
}