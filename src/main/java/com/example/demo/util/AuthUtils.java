package com.example.demo.util;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

	@Autowired
	private UserRepository userRepository;

	public String getLoggedUserEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		if (principal instanceof String) {
			return (String) principal;
		}
		return null;
	}

	public User getLoggedUser() {
		String email = getLoggedUserEmail();
		if (email == null) {
			throw new UsernameNotFoundException("Nessun utente autenticato");
		}		
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Utente non trovato: " + email);
		}
		return user;
	}

	public Long getLoggedUserId() {
		return getLoggedUser().getId();
	}
}
