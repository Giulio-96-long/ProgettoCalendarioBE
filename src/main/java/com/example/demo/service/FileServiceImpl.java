package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.fileDto.FileResponseDto;
import com.example.demo.entity.Attachment;
import com.example.demo.entity.Note;
import com.example.demo.repository.FileRepository;
import com.example.demo.repository.NoteRepository;
import com.example.demo.service.Iservice.NoteChangeHistoryService;
import com.example.demo.service.Iservice.ErrorLogService;
import com.example.demo.service.Iservice.FileService;
import com.example.demo.util.ConvertToFileBase64;

@Service
public class FileServiceImpl implements FileService {

	private final NoteRepository noteRepository;
	private final FileRepository fileRepository;
	private final NoteChangeHistoryService noteChangeHistoryService;
	private final ErrorLogService errorLogService;

	public FileServiceImpl(NoteRepository noteRepository, FileRepository fileRepository,
			NoteChangeHistoryService noteChangeHistoryService, ErrorLogService errorLogService) {
		this.noteRepository = noteRepository;
		this.fileRepository = fileRepository;
		this.noteChangeHistoryService = noteChangeHistoryService;
		this.errorLogService = errorLogService;
	}

	@Override
	public boolean newFile(long noteId, MultipartFile file, String pathFile) throws IOException {
		Optional<Note> noteOptional = noteRepository.findById(noteId);

		if (noteOptional.isEmpty()) {
			return false;
		}

		Note note = noteOptional.get();
		List<Attachment> newFiles = new ArrayList<>();
		try {
			Attachment fileEntity = ConvertToFileBase64.convertToFileEntity(file, note);
			newFiles.add(fileEntity);

			if (note.getFiles() == null) {
				note.setFiles(new ArrayList<>());
			}

		} catch (Exception e) {
			errorLogService.logError("Errore durante l'inserimento del file" + file.getName(), e);
		}

		note.getFiles().addAll(newFiles);
		noteRepository.save(note);
		noteChangeHistoryService.saveChange(note, "New File", note.getUser(), LocalDateTime.now());

		return false;
	}

	@Override
	public List<FileResponseDto> getFilesByNoteId(Long noteId) {
		List<Attachment> files = fileRepository.findByNoteId(noteId);
		return files.stream().map(file -> new FileResponseDto(file.getId(), file.getNome(), file.getBase64()))
				.collect(Collectors.toList());
	}

	@Override
	public boolean removeFile(long id) {

		Optional<Note> noteOptional = noteRepository.findById(id);

		if (noteOptional.isEmpty()) {
			return false;
		}

		Note note = noteOptional.get();

		fileRepository.deleteById(id);

		try {
			noteChangeHistoryService.saveChange(note, "Remove File", note.getUser(), LocalDateTime.now());
		} catch (Exception e) {
			errorLogService.logError("Remove File della nota id = " + note.getId(), e);
		}

		return false;
	}

	@Override
	public FileResponseDto getFileById(Long idFile) {
		Attachment attachment = fileRepository.findById(idFile)
				.orElseThrow(() -> new RuntimeException("File non trovato con id " + idFile));

		FileResponseDto dto = new FileResponseDto();
		dto.setId(attachment.getId());
		dto.setNome(attachment.getNome());
		dto.setBase64(attachment.getBase64());

		return dto;
	}

}
