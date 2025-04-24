package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.noteDto.NoteUpdateRequestDto;
import com.example.demo.service.Iservice.IErrorLogService;
import com.example.demo.service.Iservice.INoteService;


@RestController
@RequestMapping("/api/note")
public class NoteController {

	private final IErrorLogService errorLogService;
	private final INoteService noteService;
	
	public NoteController (IErrorLogService errorLogService, INoteService noteService){
		 this.errorLogService = errorLogService;
		 this.noteService = noteService;
	 }
	
	@PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> newNote(
	    @RequestParam String title,
	    @RequestParam String description,
	    @RequestParam boolean isImportant,
	    @RequestParam(required = false) String pathFile,
	    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventDate,
	    @RequestParam(required = false) MultipartFile[] files
	) { 
		try {
			var response = noteService.newNote(title, description, isImportant, eventDate, files, pathFile);
		    return ResponseEntity.ok(response);
		} catch (Exception e) {
			errorLogService.logError("newNote", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
		}
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllNote() {
		try {
			var response = noteService.getAllNote();
			 return ResponseEntity.ok(response);
		} catch (Exception e) {
			errorLogService.logError("getAllNote", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
	}
	
	@GetMapping("/getById/{id}")
	public ResponseEntity<?> getById(@PathVariable long id) {
		try {
			var response = noteService.getNoteById(id);
			 return ResponseEntity.ok(response);
		} catch (Exception e) {
			errorLogService.logError("getById", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
	}
	
	@GetMapping("/getNotesForMonth")
	public ResponseEntity<?> getNotesForMonth(@RequestParam int positionMonth) {
		try {
			var response = noteService.getNotesForMonth(positionMonth);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			errorLogService.logError("getNotesForMonth", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateNote(@RequestBody NoteUpdateRequestDto noteUpdateRequestDto) {
	    try {
	        boolean updated = noteService.updateNote(noteUpdateRequestDto);
	        return updated ? ResponseEntity.ok("Nota aggiornata con successo") :
	                         ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nota non trovata");
	    } catch (Exception e) {
	        errorLogService.logError("updateNote", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'aggiornamento");
	    }
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable Long id) {
	    try {
	        boolean removed = noteService.removeNoteById(id);
	        return removed ? ResponseEntity.ok("Nota eliminata con successo")
	                       : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nota non trovata");
	    } catch (Exception e) {
	        errorLogService.logError("deleteNote", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione");
	    }
	}
	
}
