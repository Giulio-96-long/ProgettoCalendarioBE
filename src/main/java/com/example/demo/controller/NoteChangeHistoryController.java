package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryFilterDto;
import com.example.demo.service.Iservice.IChangeHistoryService;
import com.example.demo.service.Iservice.IErrorLogService;

@RestController
@RequestMapping("/api/noteChangeHistory")
public class NoteChangeHistoryController {

	private final IErrorLogService logErrorService;
	private final IChangeHistoryService changeHistoryService;

	public NoteChangeHistoryController(IErrorLogService logErrorService, IChangeHistoryService changeHistoryService) {
		this.logErrorService = logErrorService;
		this.changeHistoryService = changeHistoryService;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/search")
	public ResponseEntity<?> filterErrors(@RequestBody NoteChangeHistoryFilterDto filter) {
		try {
			var response = changeHistoryService.getAllAndFilter(filter);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logErrorService.logError("/api/NoteChangeHistory/search", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}

	}
}
