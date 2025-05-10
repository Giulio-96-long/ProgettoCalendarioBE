package com.example.demo.dto.userDto;

public class PhotoResponseDto {

	private final byte[] data;
	private final String contentType;

	public PhotoResponseDto(byte[] data, String contentType) {
		this.data = data;
		this.contentType = contentType;
	}

	public byte[] getData() {
		return data;
	}

	public String getContentType() {
		return contentType;
	}
}
