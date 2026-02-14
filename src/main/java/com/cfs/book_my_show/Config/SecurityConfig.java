package com.cfs.book_my_show.Config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
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

        // 1. 🔥 SECURITY FILTER CHAIN
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

        // 2. 🔥 WEB SECURITY CUSTOMIZER (Nuclear Fix)
        // Yaha hum bata rahe hain ki kin URLs ko Security Check se BAHAR rakhna hai.
        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring()
                                .requestMatchers(HttpMethod.OPTIONS, "/**") // Pre-flight requests allow

                                // 🔥 CRITICAL FIX: Error page ko allow karein.
                                // Agar ye nahi kiya, to jab bhi code fategame, login popup aa jayega.
                                .requestMatchers("/error")

                                // 🔥 Aapki demand ke mutabik saari APIs ki list:
                                .requestMatchers("/api/movies", "/api/movies/**")
                                .requestMatchers("/api/bookings", "/api/bookings/**")
                                .requestMatchers("/api/users", "/api/users/**")
                                .requestMatchers("/api/shows", "/api/shows/**")
                                .requestMatchers("/api/theaters", "/api/theaters/**")

                                // Safety Net: /api/ ke baad kuch bhi ho, allow karo
                                .requestMatchers("/api/**");
        }

        // 3. 🔥 EXTERNAL CORS FILTER (Highest Priority)
        @Bean
        public FilterRegistrationBean<CorsFilter> corsFilter() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration config = new CorsConfiguration();

                config.setAllowedOrigins(Arrays.asList(
                                "https://itsmovietime.vercel.app",
                                "http://localhost:5173",
                                "http://localhost:3000"));

                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(Arrays.asList("*"));
                config.setAllowCredentials(true);

                source.registerCorsConfiguration("/**", config);

                FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
                bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
                return bean;
        }
}