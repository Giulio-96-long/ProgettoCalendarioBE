package com.example.demo.OAuth2;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.authenticationToken.JwtUtil;
import com.example.demo.dto.userDto.UserRequestDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	
    private final JwtUtil jwtUtil;
    private final UserServiceImpl userService;

    public OAuth2SuccessHandler(JwtUtil jwtUtil, UserServiceImpl userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        // Recupera o crea l'utente nel DB
        User user = userService.findByEmail(email);
        if (user == null) {
            UserRequestDto dto = new UserRequestDto();
            dto.setEmail(email);
            dto.setUsername(oauthUser.getAttribute("given_name"));
            dto.setLastname(oauthUser.getAttribute("family_name"));
            dto.setPassword(UUID.randomUUID().toString());
            userService.newUser(dto);
            user = userService.findByEmail(email);
        }

        // Genera il token
        String token = jwtUtil.generateToken(email, user.getRole());

        // Redirect al front-end con il token
        String targetUrl = "http://localhost:5501/login/index.html?token=" + token;
        response.sendRedirect(targetUrl);
    }
}