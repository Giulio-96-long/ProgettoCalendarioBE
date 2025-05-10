package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
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
import com.example.demo.service.Iservice.IUserService;
import com.example.demo.service.UserService;
import com.example.demo.service.Iservice.IErrorLogService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final IErrorLogService errorLogService;
	private final IUserService userService;

	public UserController(IUserService serviceUser, UserService userService, IErrorLogService errorLogService) {
		this.errorLogService = errorLogService;
		this.userService = userService;

	}

	@GetMapping("/profile")
	public ResponseEntity<?> whoAmI() {
		try {
			var user = userService.getCurrentUserInfo();
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			errorLogService.logError("User/me", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					  .body(Map.of("error", "Impossibile caricare il profilo"));
		}
	}
	
	
	 @PostMapping("/getOrUpdatephoto")
	    public ResponseEntity<?> uploadMyPhoto(
	            @RequestParam("file") MultipartFile file) {
	        try {	          
	            var response = userService.uploadProfileImage(file);
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            errorLogService.logError("User/me/photo", e);
	            return ResponseEntity
	                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(null);
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
	
	@PutMapping("/profile")
    public ResponseEntity<?> updateMe(@RequestBody UpdateUserRequestDto dto) {
        try {
            userService.updateCurrentUser(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            errorLogService.logError("User/me PUT", e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
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
