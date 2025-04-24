package com.example.demo.service.Iservice;

import com.example.demo.dto.userDto.UserRequestDto;

public interface IUserService {
	
	long newUser(UserRequestDto userRequestDto);
	
	long GetIdUser(String email);
	
	String getCurrentUserEmail();
}
