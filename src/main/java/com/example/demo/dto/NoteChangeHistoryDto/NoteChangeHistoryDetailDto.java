package com.example.demo.dto.NoteChangeHistoryDto;

import java.time.LocalDateTime;

import com.example.demo.dto.noteDto.NoteWithFilesDto;
import com.fasterxml.jackson.annotation.JsonFormat;

public class NoteChangeHistoryDetailDto {
	private Long historyId;
	private String changeType;
	private String modifiedBy;
	
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dataNote;
	
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime modificationDate;

	private NoteWithFilesDto note;

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(LocalDateTime modificationDate) {
		this.modificationDate = modificationDate;
	}

	public NoteWithFilesDto getNote() {
		return note;
	}

	public void setNote(NoteWithFilesDto note) {
		this.note = note;
	}

	public LocalDateTime getDataNote() {
		return dataNote;
	}

	public void setDataNote(LocalDateTime dataNote) {
		this.dataNote = dataNote;
	}
	
	

}
