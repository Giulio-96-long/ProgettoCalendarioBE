package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Attachment;
import com.example.demo.entity.Note;
import com.example.demo.util.ConvertToFileBase64;

@RestController
@RequestMapping("/api/note")
public class FileController {
	
	

	/*@PostMapping("/note/{noteId}/add-file")
	public ResponseEntity<?> addFilesToNote(
	        @PathVariable Long noteId,
	        @RequestParam("files") MultipartFile[] files,
	        @RequestParam(required = false) String pathFile) {

	    try {
	        Object noteRepository;
			Optional<Note> noteOptional = ((Object) noteRepository).findById(noteId);
	        if (noteOptional.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nota non trovata");
	        }

	        Note note = noteOptional.get();
	        List<File> newFiles = new ArrayList<>();

	        for (MultipartFile multipartFile : files) {
	            File fileEntity = ConvertToFileBase64.convertToFileEntity(multipartFile, pathFile, note);
	            newFiles.add(fileEntity);
	        }

	        if (note.getFiles() == null) {
	            note.setFiles(new ArrayList<>());
	        }

	        note.getFiles().addAll(newFiles);
	        noteRepository.save(note);

	        return ResponseEntity.ok("File aggiunti con successo");
	    } catch (Exception e) {
	        errorLogService.logError("addFilesToNote", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'aggiunta dei file");
	    }
	}*/
}
