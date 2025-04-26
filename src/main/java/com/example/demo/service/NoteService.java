package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.PersonalizedNoteDto.PersonalizedNoteResponseDto;
import com.example.demo.dto.fileDto.FileResponseDto;
import com.example.demo.dto.noteDto.NoteResponseDto;
import com.example.demo.dto.noteDto.NoteUpdateRequestDto;
import com.example.demo.dto.noteDto.NoteWithFilesDto;
import com.example.demo.entity.Attachment;
import com.example.demo.entity.DateNote;
import com.example.demo.entity.Note;
import com.example.demo.entity.PersonalizedNote;
import com.example.demo.entity.User;
import com.example.demo.repository.DateNoteRepository;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Iservice.IChangeHistoryService;
import com.example.demo.service.Iservice.IErrorLogService;
import com.example.demo.service.Iservice.IFileService;
import com.example.demo.service.Iservice.INoteService;
import com.example.demo.service.Iservice.IPersonalizedNoteService;
import com.example.demo.util.AuthUtils;
import com.example.demo.util.ConvertToFileBase64;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class NoteService implements INoteService {

	private final AuthUtils authUtils;
	private final NoteRepository noteRepository;
	private final IErrorLogService errorLogService;
	private final DateNoteRepository dateNoteRepository;
	private final IChangeHistoryService changeHistoryService;

	public NoteService(NoteRepository noteRepository, UserRepository userRepository, AuthUtils authUtils,
			DateNoteRepository dateNoteRepository, IErrorLogService errorLogService,
			IChangeHistoryService changeHistoryService, IPersonalizedNoteService personalizedNoteService,
			IFileService fileService) {
		this.noteRepository = noteRepository;
		this.authUtils = authUtils;
		this.errorLogService = errorLogService;
		this.dateNoteRepository = dateNoteRepository;
		this.changeHistoryService = changeHistoryService;
	}

	@Override
	public boolean newNote(String title, String description, boolean isImportant, String color, String message,
			LocalDateTime dateNote, MultipartFile[] files, String pathFile) throws java.io.IOException {

		User user = authUtils.getLoggedUser();
		if (user == null) {
			throw new RuntimeException("User not authenticated");
		}
		// Crea la DateNote
		DateNote newDateNote = new DateNote();
		newDateNote.setEventDate(dateNote);

		dateNoteRepository.save(newDateNote);

		// Crea la Note e associa la DateNote
		Note newNote = new Note();
		newNote.setUser(user);
		newNote.setTitle(title);
		newNote.setDescription(description);
		newNote.setImportant(isImportant);
		newNote.setDateNote(newDateNote);
		newDateNote.addNote(newNote);

		if (StringUtils.hasText(color) && StringUtils.hasText(message)) {
			PersonalizedNote personalizedNote = new PersonalizedNote();
			personalizedNote.setCustomMessage(message);
			personalizedNote.setColor(color);
			personalizedNote.setNote(newNote);
			newNote.setPersonalizedNote(personalizedNote);
		}

		if (files != null && files.length > 0) {
			List<Attachment> fileEntities = new ArrayList<>();
			for (MultipartFile multipartFile : files) {
				try {
					Attachment fileEntity = ConvertToFileBase64.convertToFileEntity(multipartFile, pathFile, newNote);
					fileEntity.setNote(newNote);
					fileEntities.add(fileEntity);
				} catch (Exception e) {
					errorLogService.logError("note/searchByMonth", e);
				}							
			}
			newNote.setFiles(fileEntities);
		}

		noteRepository.save(newNote);

		changeHistoryService.saveChange("Note", newNote, "CREATE", null, newNote.toString(), user, null);

		return true;
	}

	@Override 
	 public List<NoteResponseDto> getAllNote() 
	 { 
		 Long userId = authUtils.getLoggedUserId();
		 
		 LocalDateTime startDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		 LocalDateTime endDate= LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth())
		        .withHour(23).withMinute(59).withSecond(59).withNano(999999999);

		 List<DateNote> dateNotes = dateNoteRepository.findByUserIdAndDateRange(userId, startDate, endDate);

	        // Mappa le DateNote in NoteResponseDto
	        List<NoteResponseDto> result = new ArrayList<>();

	        for (DateNote dateNote : dateNotes) {
	            NoteResponseDto noteResponseDto = new NoteResponseDto();
	            noteResponseDto.setIdEvent(dateNote.getId());
	            noteResponseDto.setEventDate(dateNote.getEventDate());

	            // Mappa le Note in NoteWithFilesDto
	            List<NoteWithFilesDto> noteWithFilesDtos = dateNote.getNotes().stream()
	                .map(note -> {
	                    // Mappa la Note in NoteWithFilesDto
	                    NoteWithFilesDto noteWithFilesDto = new NoteWithFilesDto(
	                            note.getId(), 
	                            note.getTitle(), 
	                            note.getDescription(),
	                            note.isImportant(), 
	                            note.getPersonalizedNote() != null 
	                                ? new PersonalizedNoteResponseDto(note.getPersonalizedNote()) 
	                                : null, 
	                            note.getFiles().stream().map(FileResponseDto::new).collect(Collectors.toList())
	                    );
	                    return noteWithFilesDto;
	                })
	                .collect(Collectors.toList());

	            noteResponseDto.setNotes(noteWithFilesDtos);
	            result.add(noteResponseDto);
	        }

	        return result;
	 }

	@Override
	public List<NoteResponseDto> getNotesByMonth(int month) {
	    Long userId = authUtils.getLoggedUserId();
	    LocalDate now = LocalDate.now();
	    LocalDate targetMonth = LocalDate.of(now.getYear(), month, 1);

	    LocalDateTime start = targetMonth.atStartOfDay();
	    LocalDateTime end = targetMonth.withDayOfMonth(targetMonth.lengthOfMonth()).atTime(23, 59, 59);

	    List<DateNote> dateNotes = dateNoteRepository.findByUserIdAndDateRange(userId, start, end);

	    List<NoteResponseDto> noteResponseDtos = new ArrayList<>();

	    for (DateNote dateNote : dateNotes) {
	        List<NoteWithFilesDto> notesDto = new ArrayList<>();

	        for (Note note : dateNote.getNotes()) {
	            List<FileResponseDto> fileDtos = new ArrayList<>();

	            for (Attachment file : note.getFiles()) {
	                fileDtos.add(new FileResponseDto(file.getId(), file.getNome(), file.getPath(), file.getBase64()));
	            }

	            PersonalizedNoteResponseDto personalizedDto = null;
	            if (note.getPersonalizedNote() != null) {
	                personalizedDto = new PersonalizedNoteResponseDto(note.getPersonalizedNote());
	            }

	            notesDto.add(new NoteWithFilesDto(note.getId(), note.getTitle(), note.getDescription(),
	                    note.isImportant(), personalizedDto, fileDtos));
	        }

	        noteResponseDtos.add(new NoteResponseDto(dateNote.getId(), dateNote.getEventDate(), notesDto));
	    }

	    return noteResponseDtos;
	}

	@Override
	public List<NoteResponseDto> getNotesForMonth(int position) {
	    Long userId = authUtils.getLoggedUserId();
	    LocalDate now = LocalDate.now();
	    LocalDate targetMonth = now.plusMonths(position);

	    LocalDateTime start = targetMonth.withDayOfMonth(1).atStartOfDay();
	    LocalDateTime end = targetMonth.withDayOfMonth(targetMonth.lengthOfMonth()).atTime(23, 59, 59);

	    List<DateNote> dateNotes = dateNoteRepository.findByUserIdAndDateRange(userId, start, end);

	    List<NoteResponseDto> noteResponseDtos = new ArrayList<>();

	    for (DateNote dateNote : dateNotes) {
	        List<NoteWithFilesDto> notesDto = new ArrayList<>();

	        for (Note note : dateNote.getNotes()) {
	            List<FileResponseDto> fileDtos = new ArrayList<>();

	            for (Attachment file : note.getFiles()) {
	                fileDtos.add(new FileResponseDto(file.getId(), file.getNome(), file.getPath(), file.getBase64()));
	            }

	            PersonalizedNoteResponseDto personalizedDto = null;
	            if (note.getPersonalizedNote() != null) {
	                personalizedDto = new PersonalizedNoteResponseDto(note.getPersonalizedNote());
	            }

	            notesDto.add(new NoteWithFilesDto(note.getId(), note.getTitle(), note.getDescription(),
	                    note.isImportant(), personalizedDto, fileDtos));
	        }

	        noteResponseDtos.add(new NoteResponseDto(dateNote.getId(), dateNote.getEventDate(), notesDto));
	    }

	    return noteResponseDtos;
	}


	@Override
	public NoteWithFilesDto getNoteById(long id) {
	    Note note = noteRepository.findNoteWithFilesById(id);

	    if (note == null) {
	        throw new EntityNotFoundException("Nota non trovata con id: " + id);
	    }

	    List<FileResponseDto> fileDtos = new ArrayList<>();
	    for (Attachment file : note.getFiles()) {
	        fileDtos.add(new FileResponseDto(file.getId(), file.getNome(), file.getPath(), file.getBase64()));
	    }

	    PersonalizedNoteResponseDto personalizedDto = null;
	    if (note.getPersonalizedNote() != null) {
	        personalizedDto = new PersonalizedNoteResponseDto(note.getPersonalizedNote());
	    }

	    return new NoteWithFilesDto(
	            note.getId(),
	            note.getTitle(),
	            note.getDescription(),
	            note.isImportant(),
	            personalizedDto,
	            fileDtos
	    );
	}


	@Override
	@Transactional
	public boolean updateNote(NoteUpdateRequestDto noteUpdateRequestDto) {
	    Optional<Note> optionalNote = noteRepository.findById(noteUpdateRequestDto.getId());

	    if (optionalNote.isEmpty()) {
	        return false;
	    }

	    Note note = optionalNote.get();

	    User userId = authUtils.getLoggedUser();
	    
	    if (note.getUser().getId() != userId.getId()) {
	        throw new AccessDeniedException("Non puoi modificare questa nota");
	    }

	    // Aggiornamento campi principali
	    if (noteUpdateRequestDto.getTitle() != null) {
	        note.setTitle(noteUpdateRequestDto.getTitle());
	    }
	    if (noteUpdateRequestDto.getDescription() != null) {
	        note.setDescription(noteUpdateRequestDto.getDescription());
	    }
	    note.setImportant(noteUpdateRequestDto.isImportant());
	    note.setDateModification(LocalDateTime.now());

	    // Aggiornamento della personalized note se presente
	    if (note.getPersonalizedNote() != null) {
	        PersonalizedNote personalized = note.getPersonalizedNote();

	        if (noteUpdateRequestDto.getColor() != null) {
	            personalized.setColor(noteUpdateRequestDto.getColor());
	        }
	        if (noteUpdateRequestDto.getCustomMessage() != null) {
	            personalized.setCustomMessage(noteUpdateRequestDto.getCustomMessage());
	        }
	    }

	    noteRepository.save(note);

	    // Salvataggio nella change history
	    changeHistoryService.saveChange(
	        "Note",
	        note,
	        "UPDATE",
	        null,
	        note.toString(),
	        note.getUser(),
	        LocalDateTime.now()
	    );

	    return true;
	}
	
	@Override
	public boolean removeNoteById(long id) {
		User user = authUtils.getLoggedUser();

		// Trova la nota tramite l'id e verifica che appartenga all'utente
		Optional<Note> optionalNote = noteRepository.findById(id);

		if (optionalNote.isEmpty()) {
			return false;
		}

		Note note = optionalNote.get();

		// Verifica che la nota appartenga all'utente loggato
		if (note.getUser().getId() != user.getId()) {
			return false;
		}
		
		changeHistoryService.saveChange("Note",
				note, 
				"DELETE", 
				null, 
				note.toString(), 
				user, 
				LocalDateTime.now());


		noteRepository.delete(note);
		return true;
	}

}
