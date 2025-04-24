package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.fileDto.FileResponseDto;
import com.example.demo.dto.noteDto.NoteResponseDto;
import com.example.demo.dto.noteDto.NoteUpdateRequestDto;
import com.example.demo.entity.Attachment;
import com.example.demo.entity.Note;
import com.example.demo.entity.User;
import com.example.demo.repository.FileRepository;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Iservice.IErrorLogService;
import com.example.demo.service.Iservice.INoteService;
import com.example.demo.util.AuthUtils;
import com.example.demo.util.ConvertToFileBase64;

import io.jsonwebtoken.io.IOException;

@Service
public class NoteService implements INoteService {	
	
	private final AuthUtils authUtils;
	private final NoteRepository noteRepository;
	private final IErrorLogService errorLogService;
	
	public NoteService(NoteRepository noteRepository
			, UserRepository userRepository
			, AuthUtils authUtils
			,FileRepository fileRepository, IErrorLogService errorLogService) {
		this.noteRepository = noteRepository;		
		this.authUtils = authUtils;
		this.errorLogService = errorLogService;
		
	}

	@Override
	public boolean newNote(String title, 
			String description, 
			boolean isImportant, 
			LocalDateTime eventDate, 
			MultipartFile[] files, 
			String pathFile) throws java.io.IOException {
	    User user = authUtils.getLoggedUser();

	    Note newNote = new Note();
	    newNote.setUser(user);
	    newNote.setTitle(title);
	    newNote.setDescription(description);
	    newNote.setEventDate(eventDate);
	    newNote.setImportant(isImportant);

	    if (files != null && files.length > 0) {
	        List<Attachment> fileEntities = new ArrayList<>();
	        for (MultipartFile multipartFile : files) {
	            try {
	                Attachment fileEntity = ConvertToFileBase64.convertToFileEntity(multipartFile, pathFile, newNote);
	                fileEntities.add(fileEntity);
	            } catch (IOException e) {
	                errorLogService.logError("FileConversion", e); 
	            }
	        }
	        newNote.setFiles(fileEntities);
	    }

	    noteRepository.save(newNote);
	    return true;
	}
	
	
	@Override
	public List<NoteResponseDto> getAllNote() {
	    Long userId = authUtils.getLoggedUserId();
	    
	    LocalDate now = LocalDate.now();
	    LocalDateTime start = now.withDayOfMonth(1).atStartOfDay(); 
	    LocalDateTime end = now.withDayOfMonth(now.lengthOfMonth()).atTime(23, 59, 59); 
	   	    
	    List<Note> notes = noteRepository.findNotesByUserAndMonth(userId, start, end);

	 // Mappatura manuale delle note in DTO
	    List<NoteResponseDto> responseDtos = new ArrayList<>();
	    for (Note note : notes) {
	        NoteResponseDto dto = new NoteResponseDto();
	        dto.setId(note.getId());
	        dto.setTitle(note.getTitle());
	        dto.setDescription(note.getDescription());
	        dto.setEventDate(note.getEventDate());
	        dto.setImportant(note.isImportant());

	        // Mappatura dei file
	        List<FileResponseDto> files = new ArrayList<>();
	        for (Attachment file : note.getFiles()) {
	            FileResponseDto fileDto = new FileResponseDto();
	            fileDto.setId(file.getId());
	            fileDto.setNome(file.getNome());
	            fileDto.setPath(file.getPath());
	            fileDto.setBase64(file.getBase64());
	            files.add(fileDto);
	        }

	        dto.setFiles(files);
	        responseDtos.add(dto);
	    }

	    return responseDtos;
	}
	
	@Override
	public List<NoteResponseDto> getNotesForMonth(int position) {
	    Long userId = authUtils.getLoggedUserId();
	    LocalDate now = LocalDate.now();
	    LocalDateTime start;
	    LocalDateTime end;
	    
	    if(position == 1) {	    	
		    start = now.plusMonths(1).withDayOfMonth(1).atStartOfDay();  // Primo giorno del mese successivo
		    end = now.plusMonths(1).withDayOfMonth(now.plusMonths(1).lengthOfMonth()).atTime(23, 59, 59);  // Ultimo giorno del mese successivo

	    }else {	    	
	        start = now.minusMonths(1).withDayOfMonth(1).atStartOfDay();  // Primo giorno del mese precedente
	        end = now.minusMonths(1).withDayOfMonth(now.minusMonths(1).lengthOfMonth()).atTime(23, 59, 59);  // Ultimo giorno del mese precedente
	    }
	    
	    List<Note> notes = noteRepository.findNotesByUserAndMonth(userId, start, end);

	    return mapNotesToResponseDto(notes);  
	}
	
	private List<NoteResponseDto> mapNotesToResponseDto(List<Note> notes) {
	    List<NoteResponseDto> responseDtos = new ArrayList<>();
	    
	    for (Note note : notes) {
	        NoteResponseDto dto = new NoteResponseDto();
	        dto.setId(note.getId());
	        dto.setTitle(note.getTitle());
	        dto.setDescription(note.getDescription());
	        dto.setEventDate(note.getEventDate());
	        dto.setImportant(note.isImportant());
	        
	        // Mappatura dei file
	        List<FileResponseDto> files = new ArrayList<>();
	        for (Attachment file : note.getFiles()) {
	            FileResponseDto fileDto = new FileResponseDto();
	            fileDto.setId(file.getId());
	            fileDto.setNome(file.getNome());
	            fileDto.setPath(file.getPath());
	            fileDto.setBase64(file.getBase64());
	            files.add(fileDto);
	        }

	        dto.setFiles(files);
	        responseDtos.add(dto);
	    }

	    return responseDtos;
	}
	
	@Override
	public NoteResponseDto getNoteById(long id) {
		//Long userId = authUtils.getLoggedUserId();
		
		Note note = noteRepository.findNoteWithFilesById(id);
		NoteResponseDto dto = new NoteResponseDto();
		dto.setId(note.getId());
		dto.setTitle(note.getTitle());
		dto.setDescription(note.getDescription());
		dto.setEventDate(note.getEventDate());
		dto.setImportant(note.isImportant());
		
		List<FileResponseDto> fileDtos = new ArrayList<>();
		for (Attachment file : note.getFiles()) {
		    FileResponseDto fileDto = new FileResponseDto();
		    fileDto.setId(file.getId());
		    fileDto.setNome(file.getNome());
		    fileDto.setBase64(file.getBase64()); 
		    fileDto.setPath(file.getPath());
		    fileDtos.add(fileDto);
		}

		dto.setFiles(fileDtos);		
		 
		return null;
	}
	
	@Override
	public boolean updateNote(NoteUpdateRequestDto noteUpdateRequestDto) {
	    Optional<Note> optionalNote = noteRepository.findById(noteUpdateRequestDto.getId());

	   
	    if (optionalNote.isEmpty()) {
	        return false; 
	    }

	    Note note = optionalNote.get();

	    Long userId = authUtils.getLoggedUserId();
	    if (note.getUser().getId() != userId) {
	        throw new AccessDeniedException("Non puoi modificare questa nota");
	    }	    
	    
	    note.setTitle(noteUpdateRequestDto.getTitle() == null ? note.getTitle() : noteUpdateRequestDto.getTitle());
	    note.setDescription(noteUpdateRequestDto.getDescription() == null ? note.getDescription() : noteUpdateRequestDto.getDescription());
	    note.setImportant(noteUpdateRequestDto.isImportant());
	    note.setEventDate(noteUpdateRequestDto.getEventDate() == null ? note.getEventDate() : noteUpdateRequestDto.getEventDate());
	    note.setDataModification(LocalDateTime.now());
	    noteRepository.save(note);
	    
	    return true;
	}

	@Override
	public boolean removeNoteById(long id) {
	    Long userId = authUtils.getLoggedUserId();

	    // Trova la nota tramite l'id e verifica che appartenga all'utente
	    Optional<Note> optionalNote = noteRepository.findById(id);
	    
	    if (optionalNote.isEmpty()) {
	        return false;
	    }

	    Note note = optionalNote.get();
	    
	    // Verifica che la nota appartenga all'utente loggato
	    if (note.getUser().getId() != userId) {
	        return false; 
	    }

	    noteRepository.delete(note);  
	    return true;  
	}

	
}
