package com.example.demo.dto.noteDto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

public class NoteUpdateRequestDto {

	private long idDateNote;

	private String title;

	private String description;

	private Boolean isImportant;

	private LocalDateTime eventDate;

	private String color;

	private String customMessage;	
	 
	private MultipartFile[] files;
	
	private String pathFile;

	public NoteUpdateRequestDto() {
	}

	public long getIdDateNote() {
		return idDateNote;
	}

	public void setIdDateNote(long idDateNote) {
		this.idDateNote = idDateNote;
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

	public Boolean getIsImportant() {
		return isImportant;
	}

	public void setIsImportant(Boolean isImportant) {
		this.isImportant = isImportant;
	}

	public LocalDateTime getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDateTime eventDate) {
		this.eventDate = eventDate;
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

	public MultipartFile[] getFiles() {
		return files;
	}

	public void setFiles(MultipartFile[] files) {
		this.files = files;
	}

	public String getPathFile() {
		return pathFile;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}
	
	
}
