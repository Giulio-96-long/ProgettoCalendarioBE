package com.example.demo.dto.FeedbackDto;

import java.time.LocalDateTime;

public class FeedbackResponseDto {
	
	private Long id;
	private String subject;
	private String body;
	private LocalDateTime createdAt;
	private ReplyDto reply;
	private Boolean read;
	private LocalDateTime readAt;
	private String senderName;
	private boolean hasReply;

	public FeedbackResponseDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public ReplyDto getReply() {
		return reply;
	}

	public void setReply(ReplyDto reply) {
		this.reply = reply;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public LocalDateTime getReadAt() {
		return readAt;
	}

	public void setReadAt(LocalDateTime readAt) {
		this.readAt = readAt;
	}
	
	public String getSenderName() {
	    return senderName;
	}

	public void setSenderName(String senderName) {
	    this.senderName = senderName;
	}	

	public boolean isHasReply() {
		return hasReply;
	}

	public void setHasReply(boolean hasReply) {
		this.hasReply = hasReply;
	}



	public static class ReplyDto {
		private Long id;
		private String body;
		private LocalDateTime createdAt;
		private Boolean read;
		private LocalDateTime readAt;

		public ReplyDto() {
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

		public Boolean getRead() {
			return read;
		}

		public void setRead(Boolean read) {
			this.read = read;
		}

		public LocalDateTime getReadAt() {
			return readAt;
		}

		public void setReadAt(LocalDateTime readAt) {
			this.readAt = readAt;
		}
	}
}
