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
	
	@Query("SELECT DISTINCT n FROM Note n " +
		       "LEFT JOIN FETCH n.user u " +
		       "LEFT JOIN FETCH n.files f " +
		       "WHERE n.user.id = :userId " +
		       "AND n.eventDate BETWEEN :startDate AND :endDate " +
		       "ORDER BY n.eventDate")

	List<Note> findNotesByUserAndMonth(@Param("userId") Long userId, 
		                                    @Param("startDate") LocalDateTime startDate, 
		                                    @Param("endDate") LocalDateTime endDate);
	
	@Query("SELECT n FROM Note n "
			+ "LEFT JOIN FETCH n.files "
			+ "WHERE n.id = :id")
	Note findNoteWithFilesById(@Param("id") Long id);


}
