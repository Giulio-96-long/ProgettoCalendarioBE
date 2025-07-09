package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.Iservice.DateNoteService;

@RestController
@RequestMapping("/api/dateNote")
public class DateNoteController {

	public final DateNoteService dateNoteService;


	public DateNoteController(DateNoteService dateNoteService) {
		this.dateNoteService = dateNoteService;
	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<?> getById(@PathVariable long id) {

		var response = dateNoteService.getNotesByDateNoteId(id);
		return ResponseEntity.ok(response);

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteDateNote(@PathVariable Long id) {

		boolean removed = dateNoteService.deleteDateNoteById(id);
		return ResponseEntity.ok(removed);

	}
}
