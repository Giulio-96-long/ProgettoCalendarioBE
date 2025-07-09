package com.example.demo.util;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication instanceof AnonymousAuthenticationToken) {
			return null;
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		if (principal instanceof String) {
			String name = (String) principal;
			if ("anonymousUser".equals(name)) {
				return null;
			}
			return name;
		}
		return null;
	}
	
	public Optional<User> getLoggedUserOptional() {
		String email = getLoggedUserEmail();
		if (email == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(userRepository.findByEmail(email));
	}

	
	public User getLoggedUser() {
		return getLoggedUserOptional().orElseThrow(() -> new UsernameNotFoundException("Nessun utente autenticato"));
	}

	public Long getLoggedUserId() {
		return getLoggedUser().getId();
	}
}
