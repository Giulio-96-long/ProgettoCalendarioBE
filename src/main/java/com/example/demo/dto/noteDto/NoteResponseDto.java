package com.example.demo.dto.noteDto;

import java.time.LocalDateTime;
import java.util.List;

public class NoteResponseDto {		
	
    private long idEvent;
    private LocalDateTime eventDate;
    private List<NoteWithFilesDto> notes;

    public NoteResponseDto() {
    }

    public NoteResponseDto(Long idEvent, LocalDateTime eventDate, List<NoteWithFilesDto> notes) {
        this.idEvent = idEvent;
        this.eventDate = eventDate;
        this.notes = notes;
    }
	
	public LocalDateTime getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDateTime eventDate) {
		this.eventDate = eventDate;
	}


	public List<NoteWithFilesDto> getNotes() {
		return notes;
	}


	public void setNotes(List<NoteWithFilesDto> notes) {
		this.notes = notes;
	}


	public long getIdEvent() {
		return idEvent;
	}


	public void setIdEvent(long idEvent) {
		this.idEvent = idEvent;
	}

	
	
}
