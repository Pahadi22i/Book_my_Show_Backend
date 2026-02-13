package com.cfs.book_my_show.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                // 🔥 CSRF Disable (REST API ke liye required)
                                .csrf(csrf -> csrf.disable())

                                // 🔥 CORS Enable
                                .cors(Customizer.withDefaults())

                                // 🔥 Authorization Rules
                                .authorizeHttpRequests(auth -> auth
                                                // Sab API public
                                                .requestMatchers("/api/**").permitAll()

                                                // Baaki sab bhi allow (agar frontend directly open kare)
                                                .anyRequest().permitAll())

                                // 🔥 IMPORTANT: Basic Auth Disable (Popup problem solve)
                                .httpBasic(httpBasic -> httpBasic.disable())

                                // 🔥 Form Login bhi disable (optional but safe)
                                .formLogin(form -> form.disable());

                return http.build();
        }

        // 🔥 CORS Configuration
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration config = new CorsConfiguration();

                // ✅ Frontend URLs
                config.setAllowedOrigins(Arrays.asList(
                                "https://itsmovietime.vercel.app",
                                "http://localhost:5173",
                                "http://localhost:3000"));

                // ✅ Methods
                config.setAllowedMethods(Arrays.asList(
                                "GET", "POST", "PUT", "DELETE", "OPTIONS"));

                // ✅ Headers
                config.setAllowedHeaders(Arrays.asList(
                                "Authorization",
                                "Content-Type",
                                "X-Requested-With",
                                "Accept",
                                "Origin"));

                // ✅ Allow Cookies
                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration("/**", config);

                return source;
        }
}
