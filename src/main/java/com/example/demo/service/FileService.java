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
import com.example.demo.service.Iservice.IChangeHistoryService;
import com.example.demo.service.Iservice.IFileService;
import com.example.demo.util.ConvertToFileBase64;

@Service
public class FileService implements IFileService {

	private final NoteRepository noteRepository;
	private final FileRepository fileRepository;
	private final IChangeHistoryService changeHistoryService;

	public FileService(NoteRepository noteRepository, FileRepository fileRepository,
			IChangeHistoryService changeHistoryService) {
		this.noteRepository = noteRepository;
		this.fileRepository = fileRepository;
		this.changeHistoryService = changeHistoryService;
	}

	@Override
	public boolean newFile(long noteId, MultipartFile file, String pathFile) throws IOException {
		Optional<Note> noteOptional = noteRepository.findById(noteId);

		if (noteOptional.isEmpty()) {
			return false;
		}

		Note note = noteOptional.get();
		List<Attachment> newFiles = new ArrayList<>();

		Attachment fileEntity = ConvertToFileBase64.convertToFileEntity(file, pathFile, note);
		newFiles.add(fileEntity);

		if (note.getFiles() == null) {
			note.setFiles(new ArrayList<>());
		}

		note.getFiles().addAll(newFiles);
		noteRepository.save(note);
		changeHistoryService.saveChange(note, "New File", note.getUser(), LocalDateTime.now());

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

		changeHistoryService.saveChange(note, "Remove File", note.getUser(), LocalDateTime.now());

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
