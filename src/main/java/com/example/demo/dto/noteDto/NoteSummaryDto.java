package com.example.demo.dto.noteDto;

public class NoteSummaryDto {

	private long idNota;
	private String title;
	private boolean important;
	private String color;

	public NoteSummaryDto(long idNota, String title, boolean important, String color) {
		this.idNota = idNota;
		this.title = title;
		this.important = important;
		this.color = color;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isImportant() {
		return important;
	}

	public void setImportant(boolean important) {
		this.important = important;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public long getIdNota() {
		return idNota;
	}

	public void setIdNota(long idNota) {
		this.idNota = idNota;
	}	

}
