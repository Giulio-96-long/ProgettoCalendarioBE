package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "share_member")
public class ShareMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "share_id", nullable = false)
    private Share share;

    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private User user;      

    @Column(nullable = false)
    private boolean removedForMe = false;
    
    public ShareMember() {}

	public ShareMember(Share share, User dest) {
		this.share = share;
		this.user = dest;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Share getShare() {
		return share;
	}

	public void setShare(Share share) {
		this.share = share;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isRemovedForMe() {
		return removedForMe;
	}

	public void setRemovedForMe(boolean removedForMe) {
		this.removedForMe = removedForMe;
	}
    
}