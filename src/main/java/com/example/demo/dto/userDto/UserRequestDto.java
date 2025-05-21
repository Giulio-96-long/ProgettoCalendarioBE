package com.example.demo.dto.userDto;

import jakarta.validation.constraints.NotBlank;

public class UserRequestDto {	
	
	@NotBlank
	private String email;
	@NotBlank
	private String password;
	
	private String username;
	
	private String lastname;

	public UserRequestDto() {}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	
}
