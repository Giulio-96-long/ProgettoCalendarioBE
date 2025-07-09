package com.example.demo.service.Iservice;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.userDto.PhotoResponseDto;
import com.example.demo.dto.userDto.UpdateUserRequestDto;
import com.example.demo.dto.userDto.UserInfoResponseDto;
import com.example.demo.dto.userDto.UserRequestDto;
import com.example.demo.dto.userDto.UserResponseDto;
import com.example.demo.entity.User;

public interface UserService {
	
	long newUser(UserRequestDto userRequestDto);
	
	long GetIdUser(String email);
	
	String getCurrentUserEmail();
	
	boolean removeUser(Long userId);

	boolean changePassword(String currentPassword, String newPassword);
	
	User findByEmail(String email);

	UserInfoResponseDto getCurrentUserInfo();

	PhotoResponseDto uploadProfileImage(MultipartFile file);

	boolean updateCurrentUser(UpdateUserRequestDto dto);

	List<UserResponseDto> getAllUsers();

	List<UserResponseDto> searchUsers(String query);	
	
}
