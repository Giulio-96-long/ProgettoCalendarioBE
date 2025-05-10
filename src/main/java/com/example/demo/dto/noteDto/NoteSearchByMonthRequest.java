package com.example.demo.dto.noteDto;

public class NoteSearchByMonthRequest {
	public Integer month;
	public Integer year;
	public String order = "asc";
	
	public NoteSearchByMonthRequest() {}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
	
	
}
