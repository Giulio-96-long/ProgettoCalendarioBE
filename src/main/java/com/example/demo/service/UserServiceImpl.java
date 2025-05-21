package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.userDto.PhotoResponseDto;
import com.example.demo.dto.userDto.UpdateUserRequestDto;
import com.example.demo.dto.userDto.UserInfoResponseDto;
import com.example.demo.dto.userDto.UserRequestDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Iservice.UserService;
import com.example.demo.util.AuthUtils;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService customUserDetailsService;
	private final AuthUtils authUtils;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
			CustomUserDetailsService customUserDetailsService, AuthUtils authUtils) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.customUserDetailsService = customUserDetailsService;
		this.authUtils = authUtils;
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
		newUser.setEmail(userRequestDto.getEmail());
		newUser.setLastname(userRequestDto.getLastname());
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
		if (email == null)
			return false;

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

	@Override
	@Transactional(readOnly = true)
	public UserInfoResponseDto getCurrentUserInfo() {
		User user = authUtils.getLoggedUser();
		byte[] photo = user.getPhoto();
		byte[] photoToReturn = (photo == null || photo.length == 0) ? new byte[0] : photo;
		return new UserInfoResponseDto(user.getId(), user.getEmail(), user.getUsername(), user.getLastname(),
				photoToReturn, user.getRole());
	}

	@Override
	public PhotoResponseDto uploadProfileImage(MultipartFile file) throws IOException {
		User user = authUtils.getLoggedUser();

		user.setPhoto(file.getBytes());
		user.setPhotoContentType(file.getContentType());
		userRepository.save(user);

		byte[] img = user.getPhoto();
		if (img == null || img.length == 0) {
			new PhotoResponseDto(new byte[0], "");
		}
		return new PhotoResponseDto(img, user.getPhotoContentType());
	}

	@Override
	public void updateCurrentUser(UpdateUserRequestDto dto) throws Exception {
		User user = authUtils.getLoggedUser();
		
		// Aggiorna i campi se diversi da null/empty
		if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
			user.setUsername(dto.getUsername());
		}
		if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
			user.setEmail(dto.getEmail());
		}
		if (dto.getLastname() != null && !dto.getLastname().isBlank()) {
			user.setLastname(dto.getLastname());
		}

		userRepository.save(user);
	}

}