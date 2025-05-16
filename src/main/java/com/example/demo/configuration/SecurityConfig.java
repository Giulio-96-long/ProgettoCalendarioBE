package com.example.demo.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.OAuth2.CustomOAuth2UserService;
import com.example.demo.OAuth2.OAuth2SuccessHandler;
import com.example.demo.authenticationToken.JwtAuthenticationFilter;
import com.example.demo.util.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	 @Value("${app.cors.allowed-origins}")
	 private String allowedOrigins;
	
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private CustomOAuth2UserService oauth2UserService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
    		JwtAuthenticationFilter jwtFilter,
    		 OAuth2SuccessHandler successHandler) throws Exception {
        http
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable()) // Disabilita CSRF per testare da Postman o Swagger
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(
            	"/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**",
                "/api/auth/register",             
                "/api/auth/login" ,
                "/oauth2/authorization/**"          
            ).permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .anyRequest().hasAnyRole("USER","ADMIN")
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
         // OAuth2 Login configuration
            .oauth2Login(o -> o
                    .userInfoEndpoint(u -> u.userService(oauth2UserService))
                    .successHandler(successHandler)
                );
          

    return http.build();          
       
    }

    @Bean
    static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
    	AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
    	builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    	return builder.build();
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        config.setAllowedOrigins(origins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setExposedHeaders(List.of("X-Error-Code", "X-Error-Message"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // Permette invio cookie/Authorization header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Applica a tutte le rotte
        return source;
    }
}