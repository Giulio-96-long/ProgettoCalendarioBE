package com.example.demo.dto.FeedbackDto;

import jakarta.validation.constraints.*;

public class NewCommentRequestDto {

	@NotBlank(message = "L'oggetto non può essere vuoto")
	@Size(max = 255, message = "L'oggetto non può superare i 255 caratteri")
	private String subject;

	@NotBlank(message = "Il contenuto non può essere vuoto")
	private String body;

	public NewCommentRequestDto() {
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	};

}
