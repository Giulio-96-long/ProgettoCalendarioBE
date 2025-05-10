package com.example.demo.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "change_history")
public class NoteChangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String changeType;
    
    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;
    
    @ManyToOne
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    private LocalDateTime modificationDate = LocalDateTime.now();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(LocalDateTime modificationDate) {
		this.modificationDate = modificationDate;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}		
	
}