package com.example.demo.dto.noteDto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.dto.fileDto.FileRequestDto;

import jakarta.validation.constraints.NotNull;

public class NewNoteRequestDto {
	@NotNull
	private String title;
	
	private String description;	
	
	private boolean isImportant;
	
	private LocalDateTime eventDate;	

	private List<FileRequestDto> files;
	
	public NewNoteRequestDto() {}

	public LocalDateTime getEventDate() {
		return eventDate;
	}


	public void setEventDate(LocalDateTime eventDate) {
		this.eventDate = eventDate;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isImportant() {
		return isImportant;
	}

	public void setImportant(boolean isImportant) {
		this.isImportant = isImportant;
	}

	public List<FileRequestDto> getFiles() {
		return files;
	}

	public void setFiles(List<FileRequestDto> files) {
		this.files = files;
	}
	
	
	
}
