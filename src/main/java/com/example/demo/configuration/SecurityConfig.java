package com.example.demo.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${app.cors.allowed-origins}")
	private String allowedOrigins;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	SecurityFilterChain filterChain(
	        HttpSecurity http,
	        JwtAuthenticationFilter jwtFilter,
	        CustomOAuth2UserService oauth2UserService,  
	        OAuth2SuccessHandler successHandler
	) throws Exception {
	    http
	      .cors(Customizer.withDefaults())
	      .csrf(csrf -> csrf.disable())
	      .authorizeHttpRequests(authz -> authz
	          .requestMatchers(
	            "/v3/api-docs/**",
	            "/swagger-ui/**",
	            "/swagger-ui.html",
	            "/swagger-resources/**",
	            "/webjars/**",
	            "/api/auth/register",
	            "/api/auth/login",
	            "/oauth2/authorization/**"
	          ).permitAll()
	          .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	          
	          //  endpoint SOLO ADMIN 
	          .requestMatchers(HttpMethod.POST,  "/api/logError/filter").hasRole("ADMIN")
	          .requestMatchers(HttpMethod.GET,   "/api/logError/*"     ).hasRole("ADMIN")

	          .requestMatchers(HttpMethod.POST,  "/api/feedback/*/reply").hasRole("ADMIN")
	          .requestMatchers(HttpMethod.GET,   "/api/feedback/all"            ).hasRole("ADMIN")
	          .requestMatchers(HttpMethod.GET,   "/api/feedback/admin/**"       ).hasRole("ADMIN")

	          .requestMatchers(HttpMethod.POST,  "/api/noteChangeHistory/search").hasRole("ADMIN")

	          // tutti gli altri: USER o ADMIN      	   
	          .anyRequest().hasAnyRole("USER", "ADMIN")
	      )
	      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
	      .oauth2Login(oauth2 -> oauth2         
	          .userInfoEndpoint(u -> u.userService(oauth2UserService))
	          .successHandler(successHandler)
	      )                                        
	      .sessionManagement(sm -> sm
	          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	      )
	      .exceptionHandling(ex -> ex
	    		  .authenticationEntryPoint((req, res, authEx) -> {
	    		    String accept = req.getHeader(HttpHeaders.ACCEPT);
	    		    String frontendLogin = allowedOrigins.split(",")[0] + "/login/index.html";

	    		    if (accept != null && accept.contains("text/html")) {	    		     
	    		      res.sendRedirect(frontendLogin);
	    		    } else {	    		    
	    		      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    		      res.setContentType("application/json");
	    		      res.getWriter().write("{\"error\":\"Unauthorized\"}");
	    		    }
	    		  })
	    		  .accessDeniedHandler((req, res, accessEx) -> {
	    		      res.setStatus(HttpServletResponse.SC_FORBIDDEN);
	    		      res.setContentType("application/json");
	    		      res.getWriter().write("{\"error\":\"Forbidden\"}");
	    		  })
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