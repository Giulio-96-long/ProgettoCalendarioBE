package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.demo.authenticationToken.JwtUtil;
import com.example.demo.dto.userDto.LoginRequestDto;
import com.example.demo.dto.userDto.LoginResponseDto;
import com.example.demo.dto.userDto.UserRegisterResponseDto;
import com.example.demo.dto.userDto.UserRequestDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.service.Iservice.IErrorLogService;
import com.example.demo.service.Iservice.IUserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;

	private final IErrorLogService errorLogService;
	private final IUserService userService;

	public AuthController(IUserService serviceUser, UserService userService, IErrorLogService errorLogService) {
		this.errorLogService = errorLogService;
		this.userService = userService;

	}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto dto) {
        try {
            long id = userService.newUser(dto);
            UserRegisterResponseDto out = new UserRegisterResponseDto(
                id,
                id != 0 ? "Utente registrato con successo" : "Email già esistente"
            );
            return ResponseEntity.ok(out);
        } catch (Exception e) {
            errorLogService.logError("User/register", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Errore nella registrazione"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        try {
            // Autentico e ricevo Authentication
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            // Estraggo l’email da Authentication
            String email = auth.getName();

            // Carico l’utente dal DB e controllo esistenza
            User user = userService.findByEmail(email);
            if (user == null) {
                // forza BadCredentials se non trovo l’utente
                throw new BadCredentialsException("Credenziali non valide");
            }

            // Genero il JWT e preparo il DTO
            String token = jwtUtil.generateToken(email, user.getRole());
            long userId = userService.GetIdUser(email);
            LoginResponseDto resp = new LoginResponseDto(userId, token);
            return ResponseEntity.ok(resp);

        } catch (BadCredentialsException ex) {
            errorLogService.logError("User/login", ex);
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenziali non valide"));
        } catch (Exception ex) {
            errorLogService.logError("User/login", ex);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Errore interno del server"));
        }
    }
}
