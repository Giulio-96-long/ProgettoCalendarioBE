package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.logErrorDto.ErrorLogFilterDto;
import com.example.demo.dto.logErrorDto.LogErrorResponseDto;
import com.example.demo.service.Iservice.ErrorLogService;

@RestController
@RequestMapping("/api/logError")
public class ErrorLogController {

	private final ErrorLogService logErrorService;

	public ErrorLogController(ErrorLogService logErrorService) {
		this.logErrorService = logErrorService;
	}

	@PostMapping("/filter")
	public ResponseEntity<?> filterErrors(@RequestBody ErrorLogFilterDto filter) {

		var response = logErrorService.getAllError(filter);
		return ResponseEntity.ok(response);

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {

		LogErrorResponseDto dto = logErrorService.getErrorById(id);
		return ResponseEntity.ok(dto);
	}

}
