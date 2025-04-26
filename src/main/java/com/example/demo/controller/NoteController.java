package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

	private final INoteService noteService;
	private final IErrorLogService errorLogService;

	public NoteController(INoteService noteService, IErrorLogService errorLogService) {

		this.noteService = noteService;
		this.errorLogService = errorLogService;
	}

	@PostMapping(value = "/new")
	public ResponseEntity<?> newNote(@RequestParam("title") String title,
			@RequestParam("description") String description, @RequestParam("isImportant") Boolean isImportant,
			@RequestParam(value = "color", required = false) String color,
			@RequestParam(value = "message", required = false) String message,
			@RequestParam("dateNote") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateNote,
			@RequestParam(value = "files", required = false) MultipartFile[] files,
			@RequestParam(value = "pathFile", required = false) String pathFile) throws IOException {
		try {
			var response = noteService.newNote(title, description, isImportant != null ? isImportant : false, color,
					message, dateNote, files, pathFile);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			errorLogService.logError("note/new", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}

	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<?> getById(@PathVariable long id) {
		try {
			var response = noteService.getNoteById(id);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			errorLogService.logError("note/getById", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}

	}

	@GetMapping("/getNotesForMonth")
	public ResponseEntity<?> getNotesForMonth(@RequestParam int positionMonth) {
		try {
			var response = noteService.getNotesForMonth(positionMonth);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			errorLogService.logError("note/getNotesForMonth", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}

	}

	@GetMapping("/searchByMonth")
	public ResponseEntity<?> searchByMonth(@RequestParam(required = false) Integer month) {
		try {
			if (month == null || month == 0) {
				// Se month è nullo o 0, prendi il mese corrente
				month = LocalDate.now().getMonthValue();
			}
			var response = noteService.getNotesByMonth(month);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			errorLogService.logError("note/searchByMonth", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}

	}

	@PutMapping("/update")
	public ResponseEntity<?> updateNote(@RequestBody NoteUpdateRequestDto noteUpdateRequestDto) {
		try {
			boolean updated = noteService.updateNote(noteUpdateRequestDto);
			return ResponseEntity.ok(updated);
		} catch (Exception e) {
			errorLogService.logError("note/update", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable Long id) {
		try {
			boolean removed = noteService.removeNoteById(id);
			return ResponseEntity.ok(removed);
		} catch (Exception e) {
			errorLogService.logError("note/delete", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}

	}

}
