package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.PersonalizedNote;

@Repository
public interface PersonalizedNoteRepository extends JpaRepository<PersonalizedNote, Long>{
	
	PersonalizedNote findByNoteId(long noteId);
}
