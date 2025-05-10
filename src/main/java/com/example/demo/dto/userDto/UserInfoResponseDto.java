package com.example.demo.dto.userDto;

public class UserInfoResponseDto {

	private long id;
	private String email;
	private String username;	
	private String lastName;
	private byte[] photo;
	private String role;	
	
	public UserInfoResponseDto(
			long id, 
			String email,
			String username,			
			String lastName,
			byte[] photo,
			String role) {	
		this.id = id;
		this.email = email;
		this.username = username;
		this.lastName = lastName;
		this.photo = photo;
		this.role = role;
	}	

	public UserInfoResponseDto(String username, String role) {
		this.username = username;
		this.role = role;
	}	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String u) {
		this.username = u;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String r) {
		this.role = r;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
	
}
