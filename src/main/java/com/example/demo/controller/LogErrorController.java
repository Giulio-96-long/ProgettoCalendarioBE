package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.Iservice.IErrorLogService;

@RestController
@RequestMapping("/api/logError")
public class LogErrorController {

	private final IErrorLogService logErrorService;
	
	public LogErrorController (IErrorLogService logErrorService) {	
		this.logErrorService = logErrorService;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/")
	public ResponseEntity<?> getAllError(){
		try {
			var response = logErrorService.getAllError();	
			return ResponseEntity.ok(response);
				
		} catch (Exception e) {
			logErrorService.logError("logError", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
			
	}
	
}
