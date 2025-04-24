package com.example.demo.dto.noteDto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.dto.fileDto.FileResponseDto;


public class NoteResponseDto {
	
	private long id;	
	
	private String title;
	
	private String description;	
	
	private boolean isImportant;
	
	private LocalDateTime eventDate;
	
	private List<FileResponseDto> files;
	
	public NoteResponseDto() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public LocalDateTime getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDateTime eventDate) {
		this.eventDate = eventDate;
	}

	public List<FileResponseDto> getFiles() {
		return files;
	}

	public void setFiles(List<FileResponseDto> files) {
		this.files = files;
	}

	
	
}
