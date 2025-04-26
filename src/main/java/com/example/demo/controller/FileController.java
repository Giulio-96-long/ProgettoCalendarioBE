package com.example.demo.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.Iservice.IErrorLogService;
import com.example.demo.service.Iservice.IFileService;

@RestController
@RequestMapping("/api/file")
public class FileController {

	private final IFileService fileService;
	private final IErrorLogService errorLogService;

	public FileController(IFileService fileService, IErrorLogService errorLogService) {
		this.fileService = fileService;
		this.errorLogService = errorLogService;
	}

	@PostMapping("/addFile/{noteId}")
	public ResponseEntity<?> addFilesToNote(@PathVariable Long noteId, @RequestParam("files") MultipartFile file,
			@RequestParam(required = false) String pathFile) throws IOException {
		try {
			var response = fileService.newFile(noteId, file, pathFile);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			errorLogService.logError("file/addFile", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteFile(@PathVariable Long id) {
		try {
			boolean removed = fileService.removeFile(id);
			return ResponseEntity.ok(removed);
		} catch (Exception e) {
			errorLogService.logError("file/delete", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}

	}

}
