package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.Iservice.IDateNoteService;
import com.example.demo.service.Iservice.IErrorLogService;

@RestController
@RequestMapping("/api/dateNote")
public class DateNoteController {

	public final IDateNoteService dateNoteService;
	private final IErrorLogService errorLogService;

	public DateNoteController(IDateNoteService dateNoteService, IErrorLogService errorLogService) {
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
