package com.example.demo.dto.NoteChangeHistoryDto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.dto.PersonalizedNoteDto.PersonalizedNoteResponseDto;
import com.example.demo.dto.fileDto.FileRequestDto;

public class NoteSnapshotDto {

	public Long id;
	
	public String title;
	
	public String description;
	
	public boolean important;
		
	public LocalDateTime dateNote;
	
	public PersonalizedNoteResponseDto personalizedNote;
	
	public List<FileRequestDto> files;
	
	public NoteSnapshotDto(long id, String title, String description, boolean important, LocalDateTime eventDate,
			PersonalizedNoteResponseDto persNote, List<FileRequestDto> list) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.important = important;
		this.dateNote = eventDate;
		this.personalizedNote = persNote;
		this.files = list;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
		return important;
	}
	public void setImportant(boolean important) {
		this.important = important;
	}
	public LocalDateTime getDateNote() {
		return dateNote;
	}
	public void setDateNote(LocalDateTime dateNote) {
		this.dateNote = dateNote;
	}
	public PersonalizedNoteResponseDto getPersonalizedNote() {
		return personalizedNote;
	}
	public void setPersonalizedNote(PersonalizedNoteResponseDto personalizedNote) {
		this.personalizedNote = personalizedNote;
	}
	public List<FileRequestDto> getFiles() {
		return files;
	}
	public void setFiles(List<FileRequestDto> files) {
		this.files = files;
	}
	
	

}
