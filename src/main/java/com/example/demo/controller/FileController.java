package com.example.demo.controller;

import java.io.IOException;
import java.net.URLConnection;
import java.util.Base64;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.fileDto.FileResponseDto;
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

	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable Long id) {
	    try {
	        FileResponseDto fileDto = fileService.getFileById(id);

	        byte[] decodedBytes = Base64.getDecoder().decode(fileDto.getBase64());

	        String filename = (fileDto.getNome() != null && !fileDto.getNome().isBlank())
	            ? fileDto.getNome()
	            : "download.bin";

	        String mimeType = URLConnection.guessContentTypeFromName(filename);

	        return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream"))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
	            .body(new ByteArrayResource(decodedBytes));

	    } catch (Exception e) {
	        errorLogService.logError("file/download", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel download");
	    }
	}

}
