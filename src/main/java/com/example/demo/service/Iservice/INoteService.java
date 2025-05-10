package com.example.demo.service.Iservice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.noteDto.DateNoteSummaryDto;
import com.example.demo.dto.noteDto.NoteUpdateRequestDto;
import com.example.demo.dto.noteDto.NoteWithFilesDto;

public interface INoteService {

	long newNote(String title, String description, boolean isImportant, String color, String message, Long dateNoteId,
			LocalDateTime eventDate, MultipartFile[] files, String pathFile) throws IOException;

	NoteWithFilesDto getNoteById(long id);

	boolean updateNote(NoteUpdateRequestDto noteUpdateRequestDto);

	boolean removeNoteById(long id);

	List<DateNoteSummaryDto> getNotesForMonth(int position, String order);

	List<DateNoteSummaryDto> getNotesByMonth(int month, int year, String order);

	boolean hasImportantNotesThisWeek();

	List<DateNoteSummaryDto> getArchivedNotesByMonth(int month, int year);
	
	Boolean addArchived(long idNote);
}
