package com.example.demo.dto.fileDto;

public class FileRequestDto {
	
	 private String nome;
	 
	 private String path;
	 
	 private String base64;
	 
	 public FileRequestDto() {}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	 
}
