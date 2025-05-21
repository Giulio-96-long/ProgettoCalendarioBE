package com.example.demo.authenticationToken;


import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.Iservice.ErrorLogService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ErrorLogService errorLogService;
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();        
        return path.equals("/api/auth/login") 
        		|| path.equals("/api/auth/register");
    }   
    
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String email = jwtUtil.extractEmail(token);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null
                        && jwtUtil.validateToken(token)) {
                	
                    var authorities = List.of(new SimpleGrantedAuthority(
                        "ROLE_"+jwtUtil.extractRole(token).toUpperCase()));
                    
                    var auth = new UsernamePasswordAuthenticationToken(
                        email,
                        null, 
                        authorities);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (ExpiredJwtException ex) {       
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json");
                res.getWriter().write("{\"error\":\"Token scaduto\"}");
                errorLogService.logError(token, ex);
                return;
            } catch (JwtException ex) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json");
                res.getWriter().write("{\"error\":\"Token non valido\"}");
                errorLogService.logError(token, ex);
                return;
            }
        }
        chain.doFilter(req, res);
    }
}