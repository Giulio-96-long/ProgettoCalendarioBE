package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PersonalizedNote")
public class PersonalizedNote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "note_id")
	private Note note;

	private String color;

	@Column(name = "custom_message", columnDefinition = "TEXT")
	private String customMessage;

	public PersonalizedNote(){}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

}