package com.example.demo.dto.fileDto;

import com.example.demo.entity.Attachment;

public class FileResponseDto {
	
	private long id;
	
	private String nome;

	private String base64;

	public FileResponseDto() {
	}
	
	
	public FileResponseDto(long id, String nome, String base64) {		
		this.id = id;
		this.nome = nome;
		this.base64 = base64;
	}

	public FileResponseDto(Attachment attachment) {
        this.id = attachment.getId();
        this.nome = attachment.getNome();
        this.base64 = attachment.getBase64(); 
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

}
