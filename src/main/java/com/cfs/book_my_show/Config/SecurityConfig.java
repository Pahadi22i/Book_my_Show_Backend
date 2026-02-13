package com.cfs.book_my_show.Config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .cors(Customizer.withDefaults()) // Niche wala custom filter handle karega

                                .authorizeHttpRequests(auth -> auth
                                                // 1. OPTIONS (Pre-flight) requests allow karein
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                                // 2. 🔥 RAM-BAAN ILAJ (Master Fix):
                                                // "/api/" se start hone wale DUNIYA KE SABHI URLs ko allow kar do.
                                                // Ab aapko alag-alag users, bookings likhne ki zaroorat nahi hai.
                                                .requestMatchers("/api/**").permitAll()

                                                // Agar aapko specific hi rakhna hai to aise likhein (Dono pattern
                                                // zaroori hain):
                                                // .requestMatchers("/api/users", "/api/users/**").permitAll()
                                                // .requestMatchers("/api/bookings", "/api/bookings/**").permitAll()

                                                .anyRequest().authenticated())
                                .httpBasic(Customizer.withDefaults());

                return http.build();
        }

        // 🔥 CORS Filter (Highest Priority)
        @Bean
        public FilterRegistrationBean<CorsFilter> corsFilter() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration config = new CorsConfiguration();

                // 1. Frontend URLs (Apne production URL check kar lena)
                config.setAllowedOrigins(Arrays.asList(
                                "https://itsmovietime.vercel.app",
                                "http://localhost:5173",
                                "http://localhost:3000"));

                // 2. Allow ALL Methods
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

                // 3. Allow ALL Headers
                config.setAllowedHeaders(Arrays.asList("*"));

                // 4. Credentials
                config.setAllowCredentials(true);

                source.registerCorsConfiguration("/**", config);

                FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
                bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Security se pehle chalega
                return bean;
        }
}