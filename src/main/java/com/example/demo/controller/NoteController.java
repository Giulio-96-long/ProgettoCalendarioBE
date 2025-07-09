package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.noteDto.NoteSearchByMonthRequest;
import com.example.demo.dto.noteDto.NoteUpdateRequestDto;
import com.example.demo.dto.shareDto.ShareRequestDto;
import com.example.demo.service.Iservice.NoteService;

@RestController
@RequestMapping("/api/note")
public class NoteController {

	private final NoteService noteService;


	public NoteController(NoteService noteService) {

		this.noteService = noteService;
		
	}

	@PostMapping(value = "/new")
	public ResponseEntity<?> newNote(@RequestParam("title") String title,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam("isImportant") Boolean isImportant,
			@RequestParam(value = "color", required = false) String color,
			@RequestParam(value = "message", required = false) String message,
			@RequestParam(value = "dateNoteId", required = false) Long dateNoteId,
			@RequestParam(value = "dateNote", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateNote,
			@RequestParam(value = "files", required = false) MultipartFile[] files,
			@RequestParam(value = "shareUserIds", required = false) List<Long> shareUserIds) throws IOException {

	
		List<ShareRequestDto> recipients = new ArrayList<>();
		if (shareUserIds != null) {
			for (int i = 0; i < shareUserIds.size(); i++) {
				ShareRequestDto dto = new ShareRequestDto();
				dto.setUserId(shareUserIds.get(i));
				recipients.add(dto);
			}
		}

		var response = noteService.newNote(title, description,
				isImportant != null && isImportant, color, message,
				dateNoteId, dateNote, files, recipients);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<?> getById(@PathVariable long id) {

		var response = noteService.getNoteById(id);
		return ResponseEntity.ok(response);

	}

	@PostMapping("/searchByMonth")
	public ResponseEntity<?> searchByMonth(@RequestBody NoteSearchByMonthRequest input) {

		var response = noteService.getNotesByMonth(input.month, input.year, input.order);

		return ResponseEntity.ok(response);

	}

	@GetMapping("/getNotesForMonth")
	public ResponseEntity<?> getNotesForMonth(@RequestParam int positionMonth,
			@RequestParam(defaultValue = "asc") String order) {

		var response = noteService.getNotesForMonth(positionMonth, order);
		return ResponseEntity.ok(response);

	}

	@PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateNote(@RequestParam("idDateNote") long idDateNote,
			@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("isImportant") Boolean isImportant,
			@RequestParam(name = "eventDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventDate,
			@RequestParam(name = "color", required = false) String color,
			@RequestParam(name = "customMessage", required = false) String customMessage,
			@RequestParam(name = "pathFile", required = false) String pathFile,
			@RequestPart(name = "files", required = false) MultipartFile[] files) {

		var dto = new NoteUpdateRequestDto();
		dto.setIdDateNote(idDateNote);
		dto.setTitle(title);
		dto.setDescription(description);
		dto.setIsImportant(isImportant);
		dto.setEventDate(eventDate);
		dto.setColor(color);
		dto.setCustomMessage(customMessage);
		dto.setPathFile(pathFile);
		dto.setFiles(files);
		var updated = noteService.updateNote(dto);
		return ResponseEntity.ok(updated);

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable Long id) {

		var removed = noteService.removeNoteById(id);
		return ResponseEntity.ok(removed);

	}

	@GetMapping("/getImportantNotes")
	public ResponseEntity<?> getNotifications() {

		var response = noteService.hasImportantNotesThisWeek();
		return ResponseEntity.ok(response);

	}

	@PostMapping("/archived")
	public ResponseEntity<?> searchByMonth(@RequestBody long idNote) {

		var response = noteService.addArchived(idNote);
		return ResponseEntity.ok(response);

	}

	@GetMapping("/getArchived")
	public ResponseEntity<?> getArchivedNotesByMonth(@RequestParam(required = false) int month,
			@RequestParam(required = false) int year) {

		var response = noteService.getArchivedNotesByMonth(month, year);
		return ResponseEntity.ok(response);

	}

}
