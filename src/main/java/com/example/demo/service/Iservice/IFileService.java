package com.example.demo.service.Iservice;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.fileDto.FileResponseDto;

public interface IFileService {

	boolean newFile(long noteId, MultipartFile file, String pathFile) throws IOException;
	
	boolean removeFile(long id);

	List<FileResponseDto> getFilesByNoteId(Long noteId);
	
}
