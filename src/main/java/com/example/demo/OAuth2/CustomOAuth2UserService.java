package com.example.demo.OAuth2;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CustomUserDetailsService;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	@Autowired
    private UserRepository userRepository;                 

	  
	    @Autowired
	    private PasswordEncoder passwordEncoder; 
	    
	    @Autowired
	    private CustomUserDetailsService userDetailsService;

	    @Override
	    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
	        OAuth2User oauth = super.loadUser(req);
	        String email = oauth.getAttribute("email");
	        
	        // crea utente se non esiste
	        if (!userRepository.existsByEmail(email)) {
	            User u = new User();
	            u.setEmail(email);
	            u.setUsername(oauth.getAttribute("given_name"));
	            u.setLastname(oauth.getAttribute("family_name"));
	            u.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
	            u.setRole("USER");
	            userRepository.save(u);
	        }
	        
	        // carica i dettagli di Spring Security
	        UserDetails ud = userDetailsService.loadUserByUsername(email);
	        return new DefaultOAuth2User(
	            ud.getAuthorities(),
	            oauth.getAttributes(),
	            "email"
	        );
	    }
	}
