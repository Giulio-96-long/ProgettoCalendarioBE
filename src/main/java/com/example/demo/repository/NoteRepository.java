package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
		
	@Query("SELECT n FROM Note n "
			+ "LEFT JOIN FETCH n.files "
			+ "WHERE n.id = :id")
	Note findNoteWithFilesById(@Param("id") Long id);


}
