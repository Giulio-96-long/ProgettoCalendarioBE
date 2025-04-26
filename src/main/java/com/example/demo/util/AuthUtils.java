package com.example.demo.util;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

	   @Autowired
	    private UserRepository userRepository;

	    public String getLoggedUserEmail() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	            return userDetails.getUsername();
	        }

	        return null;
	    }

	    public Long getLoggedUserId() {
	        String email = getLoggedUserEmail();
	        if (email != null) {
	        	User user = getLoggedUser();
	            return user != null ? user.getId() : null;
	        }
	        return null;
	    }

	    public User getLoggedUser() {
	        String email = getLoggedUserEmail();
	        if (email == null) {
	            return null;
	        }
	        return userRepository.findByEmail(email);
	    }
}
