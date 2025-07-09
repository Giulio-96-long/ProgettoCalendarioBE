package com.example.demo.dto.fileDto;

public class FileRequestDto {
	
	 private String nome;
	
	 private String base64;
	 
	 public FileRequestDto() {}
	 
	 public FileRequestDto(String nome, String base64) {
		 this.nome = nome;
		 this.base64 = base64;
		 
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
