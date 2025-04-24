package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "Note")
public class Note {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = true)
	private String title;
	
	@Column(nullable = true)
	private String description ;
	
	@Column(nullable = false)
	private LocalDateTime dateCreation;
	
	@Column(nullable = true)
	private LocalDateTime dateModification;	
	
	@Column(nullable = true)
	private LocalDateTime eventDate ;	

	@Column(nullable = true)
	private boolean isImportant = false;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@OneToMany(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Attachment> files = new ArrayList<>();	
	

    public Note() {}

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDataCreation() {
		return dateCreation;
	}

	public void setDataCreation(LocalDateTime dataCreation) {
		this.dateCreation = dataCreation;
	}

	public boolean isImportant() {
		return isImportant;
	}

	public void setImportant(boolean isImportant) {
		this.isImportant = isImportant;
	}
	
	public LocalDateTime getDataModification() {
		return dateModification;
	}

	public void setDataModification(LocalDateTime dataModification) {
		this.dateModification = dataModification;
	}
	
	public User getUser() {
		return user;
		}

	public void setUser(User user) {
		this.user = user;
	}
	
	public LocalDateTime getEventDate() {
	    return eventDate;
	}

	public void setEventDate(LocalDateTime eventDate) {
	    this.eventDate = eventDate;
	}

	public LocalDateTime getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(LocalDateTime dateCreation) {
		this.dateCreation = dateCreation;
	}

	public LocalDateTime getDateModification() {
		return dateModification;
	}

	public void setDateModification(LocalDateTime dateModification) {
		this.dateModification = dateModification;
	}

	public List<Attachment> getFiles() {
		return files;
	}

	public void setFiles(List<Attachment> files) {
		this.files = files;
	}

	
}
