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
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())

                                // 1. CORS ko default rakhein (Kyunki hum niche Custom Filter use kar rahe hain)
                                .cors(Customizer.withDefaults())

                                .authorizeHttpRequests(auth -> auth
                                                // 🔥 FIX: OPTIONS requests ko hamesha allow karein (Pre-flight check ke
                                                // liye)
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                                // Public URLs
                                                .requestMatchers(
                                                                "/api/movies/**",
                                                                "/api/users/**",
                                                                "/api/bookings/**",
                                                                "/api/shows/**",
                                                                "/api/theaters/**")
                                                .permitAll()

                                                .anyRequest().authenticated())
                                .httpBasic(Customizer.withDefaults());

                return http.build();
        }

        // 🔥 SUPER FIX: FilterRegistrationBean use karein
        // Ye CORS Filter ko Spring Security se PEHLE chalayega
        @Bean
        public FilterRegistrationBean<CorsFilter> corsFilter() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration config = new CorsConfiguration();

                // 1. Frontend URLs
                config.setAllowedOrigins(Arrays.asList(
                                "https://itsmovietime.vercel.app",
                                "http://localhost:5173",
                                "http://localhost:3000"));

                // 2. Methods: "*" use karein taki OPTIONS, GET, POST sab allowed ho
                config.setAllowedMethods(Arrays.asList("*"));

                // 3. Headers: "*" use karein (Browser pre-flight me alag headers bhejta hai)
                config.setAllowedHeaders(Arrays.asList("*"));

                // 4. Credentials
                config.setAllowCredentials(true);

                source.registerCorsConfiguration("/**", config);

                // 🔥 HIGHEST PRECEDENCE set karna zaroori hai
                FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
                bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
                return bean;
        }
}