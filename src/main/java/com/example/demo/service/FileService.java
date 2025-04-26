package com.example.demo.service;

import java.io.IOException;
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
import com.example.demo.service.Iservice.IFileService;
import com.example.demo.util.ConvertToFileBase64;

@Service
public class FileService implements IFileService{

	private final NoteRepository noteRepository;
	private final FileRepository fileRepository;
	
	public FileService(NoteRepository noteRepository,FileRepository fileRepository)
	{
		this.noteRepository = noteRepository;	
		this.fileRepository = fileRepository;
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

		return false;
	}	

	@Override
	public List<FileResponseDto> getFilesByNoteId(Long noteId) {
	        // Recupera i file dalla repository
	        List<Attachment> files = fileRepository.findByNoteId(noteId);
	        // Mappa i file in FileResponseDto
	        return files.stream()
	                    .map(file -> new FileResponseDto(file.getId(), file.getNome(), file.getPath(), file.getBase64()))
	                    .collect(Collectors.toList());
	    }
	
	@Override
	public boolean removeFile(long id) {
		fileRepository.deleteById(id);
		return false;
	}

}
