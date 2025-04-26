package com.example.demo.dto.noteDto;

import java.util.List;

import com.example.demo.dto.PersonalizedNoteDto.PersonalizedNoteResponseDto;
import com.example.demo.dto.fileDto.FileResponseDto;

public class NoteWithFilesDto {
	
	private long id;  
    private String title; 
    private String description;  
    private boolean isImportant; 
    private PersonalizedNoteResponseDto personalized;  
    private List<FileResponseDto> files; 
	
    
    
    public NoteWithFilesDto(long id, String title, String description, boolean isImportant,
			PersonalizedNoteResponseDto personalized, List<FileResponseDto> files) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.isImportant = isImportant;
		this.personalized = personalized;
		this.files = files;
	}

	public NoteWithFilesDto() {}
	
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

	
	public List<FileResponseDto> getFiles() {
		return files;
	}

	public void setFiles(List<FileResponseDto> files) {
		this.files = files;
	}

	public PersonalizedNoteResponseDto getPersonalizated() {
		return personalized;
	}

	public void setPersonalizated(PersonalizedNoteResponseDto personalizated) {
		this.personalized = personalizated;
	}
	
}
