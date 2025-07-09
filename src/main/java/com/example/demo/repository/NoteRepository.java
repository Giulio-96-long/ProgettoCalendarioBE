package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

	
	@Query("""
			   SELECT n
			     FROM Note n
			LEFT JOIN FETCH n.files
			    WHERE n.id = :noteId
			 """)
	Note findNoteWithFilesById(@Param("noteId") Long noteId);

	@Query("SELECT n FROM Note n " + "JOIN FETCH n.dateNote d " + "WHERE n.user.id = :userId "
			+ "AND n.archived = false " + "AND n.isImportant = true "
			+ "AND d.eventDate BETWEEN :startDate AND :endDate")
	List<Note> findByUserIdAndDateBetween(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

}
