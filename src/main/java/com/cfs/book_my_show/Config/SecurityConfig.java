package com.cfs.book_my_show.Config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        // 1. 🔥 SECURITY FILTER CHAIN (Sirf non-API routes ke liye fallback)
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .anyRequest().authenticated() // Baki sab secure
                                )
                                .httpBasic(basic -> basic.disable())
                                .formLogin(login -> login.disable());

                return http.build();
        }

        // 2. 🔥 WEB SECURITY CUSTOMIZER (The Real Fix for Login Popup)
        // Ye URLs Security Check se poori tarah BAHAR ho jayenge.
        // Spring Security in URLs ko touch bhi nahi karega -> No Password Popup!
        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring()
                                .requestMatchers("/api/**");
        }

        // 3. 🔥 EXTERNAL CORS FILTER (The Real Fix for CORS)
        // Chunki humne Security Bypass kar di hai, isliye Security Chain wala CORS kaam
        // nahi karega.
        // Humein ye EXTERNAL filter chahiye jo sabse pehle chale.
        @Bean
        public FilterRegistrationBean<CorsFilter> corsFilter() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration config = new CorsConfiguration();

                // 1. Frontend URLs
                config.setAllowedOrigins(Arrays.asList(
                                "https://itsmovietime.vercel.app",
                                "http://localhost:5173",
                                "http://localhost:3000"));

                // 2. Methods
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

                // 3. Headers (Wildcard use karein safe side ke liye)
                config.setAllowedHeaders(Arrays.asList("*"));

                // 4. Credentials
                config.setAllowCredentials(true);

                source.registerCorsConfiguration("/**", config);

                // 🔥 HIGHEST PRECEDENCE: Ye Security se pehle chalega
                FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
                bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
                return bean;
        }
}