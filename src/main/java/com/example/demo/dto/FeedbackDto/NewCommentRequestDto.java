package com.example.demo.dto.FeedbackDto;

public class NewCommentRequestDto {

	private String subject;
    private String body;
    
    public NewCommentRequestDto() {}

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
