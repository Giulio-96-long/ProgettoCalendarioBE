package com.example.demo.dto.PersonalizedNoteDto;

import com.example.demo.entity.PersonalizedNote;

public class PersonalizedNoteResponseDto {
	private Long id;
	private Long noteId;

	private String color;
	private String customMessage;
	
	public PersonalizedNoteResponseDto(){}

	public PersonalizedNoteResponseDto(String color, String customMessage) {
	}

	public PersonalizedNoteResponseDto(PersonalizedNote personalizedNote) {
		this.id = personalizedNote.getId();
		this.noteId = personalizedNote.getNote().getId();
		this.color = personalizedNote.getColor();
		this.customMessage = personalizedNote.getCustomMessage();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNoteId() {
		return noteId;
	}

	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

}
