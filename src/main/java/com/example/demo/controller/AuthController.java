package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.demo.authenticationToken.JwtUtil;
import com.example.demo.dto.userDto.LoginRequestDto;
import com.example.demo.dto.userDto.LoginResponseDto;
import com.example.demo.dto.userDto.UserRegisterResponseDto;
import com.example.demo.dto.userDto.UserRequestDto;
import com.example.demo.entity.User;

import com.example.demo.service.Iservice.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;

	private final UserService serviceUser;

	public AuthController(UserService serviceUser) {
		this.serviceUser = serviceUser;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody @Valid UserRequestDto dto, BindingResult result) {
	    if (result.hasErrors()) {
	        return ResponseEntity.badRequest().body(Map.of("message", "Dati di registrazione non validi"));
	    }
	    long id = serviceUser.newUser(dto);
	    if (id == 0) {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body(Map.of("message", "Email già esistente"));
	    }
	    return ResponseEntity.ok(Map.of("id", id, "message", "Utente registrato con successo"));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequest, BindingResult result) {

		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body("Credenziali non valide");
		}
		// Autentico e ricevo Authentication
		Authentication auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		// Estraggo l’email da Authentication
		String email = auth.getName();

		// Carico l’utente dal DB e controllo esistenza
		User user = serviceUser.findByEmail(email);
		if (user == null) {
		    throw new BadCredentialsException("Tentativo login fallito con email: " + loginRequest.getEmail());
		}


		// Genero il JWT e preparo il DTO
		String token = jwtUtil.generateToken(email, user.getRole());
		long userId = serviceUser.GetIdUser(email);
		LoginResponseDto resp = new LoginResponseDto(userId, token);
		return ResponseEntity.ok(resp);

	}

}
