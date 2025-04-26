package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.authenticationToken.JwtUtil;
import com.example.demo.dto.userDto.ChangePasswordRequestDto;
import com.example.demo.dto.userDto.LoginRequestDto;
import com.example.demo.dto.userDto.LoginResponseDto;
import com.example.demo.dto.userDto.UserRegisterResponseDto;
import com.example.demo.dto.userDto.UserRequestDto;
import com.example.demo.entity.User;
import com.example.demo.service.Iservice.IUserService;
import com.example.demo.service.UserService;
import com.example.demo.service.Iservice.IErrorLogService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;

	private final IErrorLogService errorLogService;
	private final IUserService userService;

	public UserController(IUserService serviceUser, UserService userService, IErrorLogService errorLogService) {
		this.errorLogService = errorLogService;
		this.userService = userService;

	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserRequestDto userRequestDto) {
		try {
			UserRegisterResponseDto registerDto = new UserRegisterResponseDto();
			long created = userService.newUser(userRequestDto);
			if (created != 0) {
				registerDto.setId(created);
				registerDto.setResponse("Utente registrato con successo");
				return ResponseEntity.ok(registerDto);

			} else {
				registerDto.setId(created);
				registerDto.setResponse("Email già esistente");
				return ResponseEntity.ok(registerDto);
			}
		} catch (Exception e) {
			errorLogService.logError("User/register", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			User user = userService.findByEmail(loginRequest.getEmail());
			String token = jwtUtil.generateToken(loginRequest.getEmail(), user.getRole());
			long id = userService.GetIdUser(loginRequest.getEmail());

			LoginResponseDto response = new LoginResponseDto(id, token);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			errorLogService.logError("User/login", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}

	}

	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDto request) {
		try {
			boolean success = userService.changePassword(request.getCurrentPassword(), request.getNewPassword());
			return ResponseEntity.ok(success);
		} catch (Exception e) {
			errorLogService.logError("User/changePassword", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteFile(@PathVariable Long id) {
		try {
			boolean removed = userService.removeUser(id);
			return ResponseEntity.ok(removed);
		} catch (Exception e) {
			errorLogService.logError("User/changePassword", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
	}

}
