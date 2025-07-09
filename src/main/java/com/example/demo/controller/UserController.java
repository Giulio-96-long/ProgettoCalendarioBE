package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.userDto.ChangePasswordRequestDto;
import com.example.demo.dto.userDto.UpdateUserRequestDto;
import com.example.demo.dto.userDto.UserResponseDto;
import com.example.demo.service.Iservice.UserService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/profile")
	public ResponseEntity<?> whoAmI() {

		var user = userService.getCurrentUserInfo();
		return ResponseEntity.ok(user);

	}

	@PostMapping("/getOrUpdatephoto")
	public ResponseEntity<?> uploadMyPhoto(@RequestParam("file") MultipartFile file) {

		var response = userService.uploadProfileImage(file);
		return ResponseEntity.ok(response);

	}

	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDto request) {

		boolean success = userService.changePassword(request.getCurrentPassword(), request.getNewPassword());
		return ResponseEntity.ok(success);

	}

	@PutMapping("/profile")
	public ResponseEntity<?> updateMe(@RequestBody UpdateUserRequestDto dto) {

		boolean success = userService.updateCurrentUser(dto);
		return ResponseEntity.ok(success);

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteFile(@PathVariable Long id) {

		boolean removed = userService.removeUser(id);
		return ResponseEntity.ok(removed);

	}

	@GetMapping("allUser")
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		List<UserResponseDto> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/search")
	public ResponseEntity<List<UserResponseDto>> searchUsers(@RequestParam("query") String query) {
		List<UserResponseDto> results = userService.searchUsers(query);
		return ResponseEntity.ok(results);
	}

}
