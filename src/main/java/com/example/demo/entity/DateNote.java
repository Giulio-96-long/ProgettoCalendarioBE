package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@SQLRestriction("deleted = false")
@SQLDelete(sql = "UPDATE DateNote SET deleted = true WHERE id = ?")
@Table(name = "DateNote")
public class DateNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(
    	      mappedBy = "dateNote",
    	      cascade = {},          
    	      orphanRemoval = false
    	    )
    private List<Note> notes = new ArrayList<>(); 

    @Column(nullable = false)
    private LocalDateTime eventDate;
    
	@Column(nullable = false)
	private boolean deleted = false;
    
    @PrePersist
    public void prePersist(){
      if (eventDate == null) {      
        this.eventDate = LocalDateTime.now();
      }
    }
    
    public DateNote() {}

    public void addNote(Note note) {
        this.notes.add(note);
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public LocalDateTime getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDateTime eventDate) {
		this.eventDate = eventDate;
	}
    
    
}