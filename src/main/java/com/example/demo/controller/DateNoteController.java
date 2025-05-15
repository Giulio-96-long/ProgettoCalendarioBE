package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.Iservice.DateNoteService;
import com.example.demo.service.Iservice.ErrorLogService;

@RestController
@RequestMapping("/api/dateNote")
public class DateNoteController {

	public final DateNoteService dateNoteService;
	private final ErrorLogService errorLogService;

	public DateNoteController(DateNoteService dateNoteService, ErrorLogService errorLogService) {
		this.dateNoteService = dateNoteService;
		this.errorLogService = errorLogService;

	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<?> getById(@PathVariable long id) {
		try {
			var response = dateNoteService.getNotesByDateNoteId(id);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			errorLogService.logError("DateNote/getById", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteDateNote(@PathVariable Long id) {
		try {
			boolean removed = dateNoteService.deleteDateNoteById(id);
			return ResponseEntity.ok(removed);
		} catch (Exception e) {
			errorLogService.logError("DateNote/delete", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}

	}
}
