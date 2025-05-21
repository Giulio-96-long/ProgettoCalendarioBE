package com.example.demo.dto.userDto;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordRequestDto {
	
	@NotBlank
	private String currentPassword;
	
	@NotBlank
	private String newPassword;

	public ChangePasswordRequestDto() {
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
