package com.example.demo.service.Iservice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.noteDto.NoteResponseDto;
import com.example.demo.dto.noteDto.NoteUpdateRequestDto;
import com.example.demo.dto.noteDto.NoteWithFilesDto;

public interface INoteService {

	boolean newNote(String title, String description, boolean isImportant,String color, String message, LocalDateTime eventDate, MultipartFile[] files, String pathFile) throws IOException;
	
	List<NoteResponseDto> getAllNote();

	NoteWithFilesDto getNoteById(long id);
	
	boolean updateNote(NoteUpdateRequestDto noteUpdateRequestDto);

	boolean removeNoteById(long id);

	List<NoteResponseDto> getNotesForMonth(int position);

	List<NoteResponseDto> getNotesByMonth(int month);
}
