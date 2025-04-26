package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PersonalizedNoteDto.PersonalizedNoteResponseDto;
import com.example.demo.dto.fileDto.FileResponseDto;
import com.example.demo.dto.noteDto.NoteWithFilesDto;
import com.example.demo.entity.Attachment;
import com.example.demo.entity.DateNote;
import com.example.demo.entity.Note;
import com.example.demo.repository.DateNoteRepository;
import com.example.demo.service.Iservice.IChangeHistoryService;
import com.example.demo.service.Iservice.IDateNoteService;

import jakarta.transaction.Transactional;

@Service
public class DateNoteService implements IDateNoteService {

	private final DateNoteRepository dateNoteRepository;
	private final IChangeHistoryService changeHistoryService;

	DateNoteService(DateNoteRepository dateNoteRepository, IChangeHistoryService changeHistoryService) {
		this.dateNoteRepository = dateNoteRepository;
		this.changeHistoryService = changeHistoryService;
	}

	@Override
	public List<NoteWithFilesDto> getNotesByDateNoteId(long idDateNote) {

		java.util.Optional<DateNote> dateNoteOptional = dateNoteRepository.findById(idDateNote);

		if (dateNoteOptional.isEmpty()) {
			return new ArrayList<>();
		}

		DateNote dateNote = dateNoteOptional.get();
		List<NoteWithFilesDto> noteDtos = new ArrayList<>();

		for (Note note : dateNote.getNotes()) {
			List<FileResponseDto> fileDtos = new ArrayList<>();

			for (Attachment file : note.getFiles()) {
				fileDtos.add(new FileResponseDto(file.getId(), file.getNome(), file.getPath(), file.getBase64()));
			}

			PersonalizedNoteResponseDto personalizedDto = null;
			if (note.getPersonalizedNote() != null) {
				personalizedDto = new PersonalizedNoteResponseDto(note.getPersonalizedNote());
			}

			noteDtos.add(new NoteWithFilesDto(note.getId(), note.getTitle(), note.getDescription(), note.isImportant(),
					personalizedDto, fileDtos));
		}

		return noteDtos;
	}

	@Override
	@Transactional
	public boolean deleteDateNoteById(long idDateNote) {
		Optional<DateNote> dateNoteOptional = dateNoteRepository.findById(idDateNote);

		if (dateNoteOptional.isEmpty()) {
			return false;
		}

		DateNote dateNote = dateNoteOptional.get();

		for (Note note : dateNote.getNotes()) {
			changeHistoryService.saveChange("Note", note, "DELETE", null, note.toString(), note.getUser(),
					LocalDateTime.now());

			dateNoteRepository.delete(dateNote);
		}
		
		return true;
	}
}
