package com.example.demo.util;

import java.io.IOException;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Attachment;
import com.example.demo.entity.Note;

public class ConvertToFileBase64{
	
	public static Attachment convertToFileEntity(MultipartFile multipartFile, String pathFile, Note note) throws IOException {
	    Attachment file = new Attachment();
	    file.setNome(multipartFile.getOriginalFilename());
	    file.setBase64(Base64.getEncoder().encodeToString(multipartFile.getBytes()));
	    file.setNote(note);
	    return file;
	}
}
