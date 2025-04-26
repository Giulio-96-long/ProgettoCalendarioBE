package com.example.demo.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "change_history")
public class ChangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modifiedEntity;

    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;

    private String changeType;

    @Column(length = 5000)
    private String previousData;

    @Column(length = 5000)
    private String newData;

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

	public String getModifiedEntity() {
		return modifiedEntity;
	}

	public void setModifiedEntity(String modifiedEntity) {
		this.modifiedEntity = modifiedEntity;
	}	

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getPreviousData() {
		return previousData;
	}

	public void setPreviousData(String previousData) {
		this.previousData = previousData;
	}

	public String getNewData() {
		return newData;
	}

	public void setNewData(String newData) {
		this.newData = newData;
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