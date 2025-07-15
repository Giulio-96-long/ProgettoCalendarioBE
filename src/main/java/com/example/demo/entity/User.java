package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@Table(name = "users")
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

	@Column(nullable = false, unique = true)
	@Size(max = 100, message = "L'email non può superare 100 caratteri.")
	private String email;

	@Column(nullable = false)

	@Size(min = 6, max = 100, message = "La password deve avere tra 6 e 100 caratteri.")
	private String password;

	@Column(nullable = false)
	@Size(max = 50, message = "Lo username non può superare 50 caratteri.")
	private String username;

	@Column(nullable = false)
	@Size(max = 50, message = "Il cognome non può superare 50 caratteri.")
	private String lastname;

	@Lob
	@Column(name = "photo", columnDefinition = "MEDIUMBLOB", nullable = true)
	@Size(max = 10485760, message = "La foto non può superare 10MB.") 
	private byte[] photo;

	@Column(name = "photo_content_type", nullable = true)
	private String photoContentType;

	@Column(nullable = false)
	private boolean deleted = false;

	private String role = "USER";

	@OneToMany(mappedBy = "user", cascade = {}, orphanRemoval = false)
	private List<Note> notes = new ArrayList<>();

	@OneToMany(mappedBy = "modifiedBy", cascade = {}, orphanRemoval = false)
	private List<NoteChangeHistory> changeHistoryList;

	@OneToMany(mappedBy = "user", cascade = {}, orphanRemoval = false)
	private List<ErrorLog> errorLogs = new ArrayList<>();

	public User() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public long getId() {
		return id;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public List<NoteChangeHistory> getChangeHistoryList() {
		return changeHistoryList;
	}

	public void setChangeHistoryList(List<NoteChangeHistory> changeHistoryList) {
		this.changeHistoryList = changeHistoryList;
	}

	public List<ErrorLog> getErrorLogs() {
		return errorLogs;
	}

	public void setErrorLogs(List<ErrorLog> errorLogs) {
		this.errorLogs = errorLogs;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPhotoContentType() {
		return photoContentType;
	}

	public void setPhotoContentType(String photoContentType) {
		this.photoContentType = photoContentType;
	}

}
