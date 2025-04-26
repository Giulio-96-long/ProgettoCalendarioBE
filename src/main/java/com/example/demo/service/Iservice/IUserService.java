package com.example.demo.service.Iservice;

import com.example.demo.dto.userDto.UserRequestDto;
import com.example.demo.entity.User;

public interface IUserService {
	
	long newUser(UserRequestDto userRequestDto);
	
	long GetIdUser(String email);
	
	String getCurrentUserEmail();
	
	boolean removeUser(Long userId);

	boolean changePassword(String currentPassword, String newPassword);
	
	User findByEmail(String email);	
}
