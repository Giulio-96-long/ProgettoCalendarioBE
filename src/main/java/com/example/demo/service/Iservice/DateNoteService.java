package com.example.demo.service.Iservice;

import com.example.demo.dto.noteDto.DateNoteDetailDto;

public interface DateNoteService {

	DateNoteDetailDto getNotesByDateNoteId(long idDateNote);

	boolean deleteDateNoteById(long idDateNote);

}
