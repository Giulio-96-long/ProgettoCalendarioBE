package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PersonalizedNoteDto.PersonalizedNoteResponseDto;
import com.example.demo.dto.fileDto.FileResponseDto;
import com.example.demo.dto.noteDto.DateNoteDetailDto;
import com.example.demo.dto.noteDto.NoteWithFilesDto;
import com.example.demo.entity.DateNote;
import com.example.demo.entity.Note;
import com.example.demo.repository.DateNoteRepository;
import com.example.demo.service.Iservice.IChangeHistoryService;
import com.example.demo.service.Iservice.IDateNoteService;

import jakarta.persistence.EntityNotFoundException;
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
	public DateNoteDetailDto getNotesByDateNoteId(long idDateNote) {
		DateNote dn = dateNoteRepository.findById(idDateNote)
				.orElseThrow(() -> new EntityNotFoundException("DateNote non trovato"));

		List<NoteWithFilesDto> noteDtos = new ArrayList<>();
		for (Note note : dn.getNotes()) {
			List<FileResponseDto> fileDtos = note.getFiles().stream()
					.map(f -> new FileResponseDto(f.getId(), f.getNome(), f.getBase64())).toList();

			PersonalizedNoteResponseDto pdto = note.getPersonalizedNote() != null
					? new PersonalizedNoteResponseDto(note.getPersonalizedNote())
					: null;

			noteDtos.add(new NoteWithFilesDto(note.getId(), note.getTitle(), note.getDescription(), note.isImportant(),
					pdto, fileDtos));
		}

		return new DateNoteDetailDto(dn.getId(), dn.getEventDate(), noteDtos);
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
			changeHistoryService.saveChange(				
					note,
					"DELETE",
					note.getUser(),
					LocalDateTime.now());

			dateNoteRepository.delete(dateNote);
		}

		return true;
	}
}
