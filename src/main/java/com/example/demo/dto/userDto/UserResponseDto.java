package com.example.demo.dto.userDto;

public class UserResponseDto {

	private Long id;
	private String email;
	private String username;

	public UserResponseDto(Long id, String email, String username) {
		this.id = id;
		this.email = email;
		this.username = username;
	}
	
	public UserResponseDto() {}
	
	public void setId(Long id) {
		this.id = id;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}
}
