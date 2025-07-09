package com.example.demo.dto.shareDto;

public class ShareRequestDto {
	
	  private Long userId;
	  
	  private boolean canModify;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isCanModify() {
		return canModify;
	}

	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}
	  
	  
}
