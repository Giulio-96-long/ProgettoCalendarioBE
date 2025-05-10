package com.example.demo.dto.noteDto;

import java.time.LocalDateTime;
import java.util.List;

public class DateNoteSummaryDto {

	private Long idDataNote;
	private LocalDateTime eventDate;
	private List<NoteSummaryDto> notes;

	public DateNoteSummaryDto(Long idDataNote, LocalDateTime eventDate, List<NoteSummaryDto> notes) {
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

	public List<NoteSummaryDto> getNotes() {
		return notes;
	}

	public void setNotes(List<NoteSummaryDto> notes) {
		this.notes = notes;
	}

}
