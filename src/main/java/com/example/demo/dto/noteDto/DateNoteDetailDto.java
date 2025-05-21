package com.example.demo.dto.noteDto;

import java.time.LocalDateTime;
import java.util.List;

public class DateNoteDetailDto {
	
	private Long idDataNote;
	
	private LocalDateTime eventDate;
	private List<NoteWithFilesDto> notes;

	public DateNoteDetailDto(Long idDataNote, LocalDateTime eventDate, List<NoteWithFilesDto> notes) {
		this.idDataNote = idDataNote;
		this.eventDate = eventDate;
		this.notes = notes;
	}

	public Long getIdDataNote() {
		return idDataNote;
	}

	public void setIdDataNote(Long idDataNote) {
		this.idDataNote = idDataNote;
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

}
