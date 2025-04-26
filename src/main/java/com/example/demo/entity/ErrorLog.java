package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ErrorLog")
public class ErrorLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "endpoint")
	private String endpoint;

	@Column(name = "error_message", length = 5000)
	private String errorMessage;

	@Column(name = "stack_trace", length = 5000)
	private String stackTrace;

	@Column(name = "user")
	private String user;

	private LocalDateTime timestamp;

	public ErrorLog() {
	}

	public Long getId() {
		return id;
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

	public String getUser() {
		return user;
	}

	public void setUser(String account) {
		this.user = account;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
