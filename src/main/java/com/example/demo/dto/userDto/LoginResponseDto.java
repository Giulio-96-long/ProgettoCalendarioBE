package com.example.demo.dto.userDto;

public class LoginResponseDto {
	
		private String token;
	    private Long id;	    

	    public LoginResponseDto() {}
	    
	    public LoginResponseDto(Long id, String token) {
	        this.id = id;
	        this.token = token;
	    }

	    public Long getId() {
	        return id;
	    }

	    public String getToken() {
	        return token;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public void setToken(String token) {
	        this.token = token;
	    }
	}