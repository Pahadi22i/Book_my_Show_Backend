package com.cfs.book_my_show.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Ye import zaroori hai
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
                // 1. CSRF Disable (API ke liye zaroori)
                .csrf(csrf -> csrf.disable())

                // 2. CORS Configuration ko link karein
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. Rules set karein
                .authorizeHttpRequests(auth -> auth
                        // GET requests (Movies dekhna) sabke liye allowed hai (Public)
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()

                        // Login, Register endpoints bhi public hone chahiye
                        .requestMatchers("/api/auth/**").permitAll()

                        // Baaki sab (Add/Delete/Update) ke liye Password chahiye
                        .anyRequest().authenticated())
                // 4. Basic Auth (Popup) enable karein
                .httpBasic(org.springframework.security.config.Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Frontend URLs allow karein
        configuration.setAllowedOrigins(List.of(
                "https://itsmovietime.vercel.app", // Aapka Live App
                "http://localhost:5173" // Aapka Local Test App
        ));

        // Methods allow karein
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers allow karein
        configuration.setAllowedHeaders(List.of("*"));

        // Credentials allow karein
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}