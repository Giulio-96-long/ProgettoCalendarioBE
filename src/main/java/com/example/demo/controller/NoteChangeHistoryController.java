package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryDetailDto;
import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryFilterDto;
import com.example.demo.service.Iservice.NoteChangeHistoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/noteChangeHistory")
public class NoteChangeHistoryController {

	private final NoteChangeHistoryService noteChangeHistoryService;

	public NoteChangeHistoryController(NoteChangeHistoryService noteChangeHistoryService) {

		this.noteChangeHistoryService = noteChangeHistoryService;
	}

	@PostMapping("/search")
	public ResponseEntity<?> filterErrors(@RequestBody @Valid NoteChangeHistoryFilterDto filter, BindingResult result) {
		var response = noteChangeHistoryService.getAllAndFilter(filter);
		return ResponseEntity.ok(response);
	}
	
	 @GetMapping("/{id}")
	    public ResponseEntity<NoteChangeHistoryDetailDto> getDetail(@PathVariable Long id) {
	        NoteChangeHistoryDetailDto detail = noteChangeHistoryService.getById(id);
	        return ResponseEntity.ok(detail);
	    }
}
