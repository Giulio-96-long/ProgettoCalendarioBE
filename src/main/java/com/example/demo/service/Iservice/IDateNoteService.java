package com.example.demo.service.Iservice;

import java.util.List;

import com.example.demo.dto.noteDto.NoteWithFilesDto;

public interface IDateNoteService {

	List<NoteWithFilesDto> getNotesByDateNoteId(long idDateNote);

	boolean deleteDateNoteById(long idDateNote);

}
