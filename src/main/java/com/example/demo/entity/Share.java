package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "note_share")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    @ManyToOne @JoinColumn(name = "shared_by", nullable = false)
    private User sharedBy;

    @Column(nullable = false)
    private LocalDateTime sharedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "share", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShareMember> members = new ArrayList<>();
    
    public Share() {}

	public Share(Note note, User user) {
		this.note = note;
		this.sharedBy = user;
	}

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

	public User getSharedBy() {
		return sharedBy;
	}

	public void setSharedBy(User sharedBy) {
		this.sharedBy = sharedBy;
	}

	public LocalDateTime getSharedAt() {
		return sharedAt;
	}

	public void setSharedAt(LocalDateTime sharedAt) {
		this.sharedAt = sharedAt;
	}

	public List<ShareMember> getMembers() {
		return members;
	}

	public void setMembers(List<ShareMember> members) {
		this.members = members;
	}
    
    
}
