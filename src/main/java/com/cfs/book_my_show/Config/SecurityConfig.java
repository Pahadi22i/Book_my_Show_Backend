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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        // 1. 🔥 SECURITY FILTER CHAIN (Basic setup to disable defaults)
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .anyRequest().authenticated() // Fallback rule
                                )
                                .httpBasic(basic -> basic.disable()) // Login Popup Disable
                                .formLogin(login -> login.disable()); // Form Login Disable

                return http.build();
        }

        // 2. 🔥 WEB SECURITY CUSTOMIZER (The Real Fix for Login Popup)
        // Ye URLs Security Check se poori tarah BAHAR ho jayenge.
        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring()
                                .requestMatchers("/api/**");
        }

        // 3. 🔥 EXTERNAL CORS FILTER (The Real Fix for CORS Error)
        // Ye filter sabse pehle chalega aur Browser ko headers dega
        @Bean
        public FilterRegistrationBean<CorsFilter> corsFilter() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration config = new CorsConfiguration();

                // ✅ Frontend URLs (Live + Local)
                config.setAllowedOrigins(Arrays.asList(
                                "https://itsmovietime.vercel.app",
                                "http://localhost:5173",
                                "http://localhost:3000"));

                // ✅ Allow All Methods
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

                // ✅ Allow All Headers
                config.setAllowedHeaders(Arrays.asList("*"));

                // ✅ Allow Credentials
                config.setAllowCredentials(true);

                source.registerCorsConfiguration("/**", config);

                // 🔥 HIGHEST PRECEDENCE: Ye Security se pehle chalega
                FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
                bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
                return bean;
        }
}