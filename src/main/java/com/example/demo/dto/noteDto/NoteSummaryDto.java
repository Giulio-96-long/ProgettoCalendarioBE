package com.example.demo.dto.noteDto;

public class NoteSummaryDto {

	private String title;
	private boolean important;
	private String color;

	public NoteSummaryDto(String title, boolean important, String color) {
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

}
