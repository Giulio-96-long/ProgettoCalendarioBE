package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.userDto.UserRequestDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Iservice.IUserService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class UserService implements IUserService {
	
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public long newUser(UserRequestDto userRequestDto) {
        User existingUser = userRepository.findByEmail(userRequestDto.getEmail());
        if (existingUser != null)
            return 0;
        User newUser = new User();
        newUser.setUsername(userRequestDto.getUsername());
        newUser.setEmail(userRequestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        newUser.setRole("USER");
        userRepository.save(newUser);
        return newUser.getId();
    }

    public UserDetails loadUserByUsename(String email) throws UsernameNotFoundException {
        return customUserDetailsService.loadUserByUsername(email);
    }
    
    @Override
    public long GetIdUser(String email) {
    	 User user = userRepository.findByEmail(email);
    	 return user.getId();
    }
    
    @Override
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername(); 
        }

        return null;
    }
    
    @Override
    public boolean changePassword(String currentPassword, String newPassword) {
        String email = getCurrentUserEmail();
        if (email == null) return false;

        User user = userRepository.findByEmail(email);           

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false; 
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

	@Override
	public boolean removeUser(Long userId) {
		userRepository.deleteById(userId);
		return false;
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);		
	}

}