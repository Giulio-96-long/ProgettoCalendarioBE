package com.example.demo.dto.logErrorDto;

import java.time.LocalDateTime;

public class LogErrorResposeDto {

	  private String endpoint;
	  private String errorMessage;
	  private String stackTrace;
	  private String account;
	  
	  public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	private LocalDateTime timestamp;

	  public LogErrorResposeDto() {   
	  }

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime localDateTime) {
		this.timestamp = localDateTime;
	}
	  
	  
}
