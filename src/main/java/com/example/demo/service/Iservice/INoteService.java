package com.example.demo.service.Iservice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.noteDto.NoteResponseDto;
import com.example.demo.dto.noteDto.NoteUpdateRequestDto;

public interface INoteService {

	boolean newNote(String title, String description, boolean isImportant, LocalDateTime eventDate, MultipartFile[] files, String pathFile) throws IOException;
	
	List<NoteResponseDto> getAllNote();

	NoteResponseDto getNoteById(long id);
	
	boolean updateNote(NoteUpdateRequestDto noteUpdateRequestDto);

	boolean removeNoteById(long id);

	List<NoteResponseDto> getNotesForMonth(int position);
}
