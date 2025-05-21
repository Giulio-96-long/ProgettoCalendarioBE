package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "note")
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE note SET deleted = true WHERE id = ?")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)    
    @Size(max = 100, message = "Il titolo non può superare 100 caratteri.")
    private String title;

    @Column(nullable = true)
    @Size(max = 1000, message = "La descrizione non può superare 1000 caratteri.")
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = true)
    private LocalDateTime dateModification;

    @Column(nullable = true)
    private boolean isImportant = false;

    @Column(nullable = false)
    private boolean archived = false;

    @Column(nullable = false)
    private boolean deleted = false;   

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> files = new ArrayList<>();

    @OneToMany(mappedBy = "note", cascade = {}, orphanRemoval = false)
    private List<NoteChangeHistory> changeHistoryList = new ArrayList<>();

    @OneToOne(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true)
    private PersonalizedNote personalizedNote;

    @ManyToOne
    @JoinColumn(name = "date_note_id", nullable = false)
    private DateNote dateNote;

	public DateNote getDateNote() {
		return dateNote;
	}

	public void setDateNote(DateNote dateNote) {
		this.dateNote = dateNote;
	}

	public Note() {
	}

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

	public List<NoteChangeHistory> getChangeHistoryList() {
		return changeHistoryList;
	}

	public void setChangeHistoryList(List<NoteChangeHistory> changeHistoryList) {
		this.changeHistoryList = changeHistoryList;
	}

	public PersonalizedNote getPersonalizedNote() {
		return personalizedNote;
	}

	public void setPersonalizedNote(PersonalizedNote personalizedNote) {
		this.personalizedNote = personalizedNote;
	}

	public boolean getIsArchived() {
		return archived;
	}

	public void setIsArchived(boolean archived) {
		this.archived = archived;
	}

}
