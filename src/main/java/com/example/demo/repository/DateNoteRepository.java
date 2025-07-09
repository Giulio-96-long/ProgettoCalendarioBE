package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DateNote;

@Repository
public interface DateNoteRepository extends JpaRepository<DateNote, Long> {


	@Query("""
		      SELECT DISTINCT d
		        FROM DateNote d
		        JOIN FETCH d.notes n
		       WHERE d.id       = :dateNoteId
		         AND n.archived = false
		    """)
	DateNote findActiveByNoteId( @Param("dateNoteId") Long dateNoteId);	
	
	
	List<DateNote> findByEventDate(LocalDate eventDate);

	@Query("SELECT d FROM DateNote d " 
			+ "JOIN d.notes n " 
			+ "WHERE n.user.id = :userId " 
			+ "AND n.archived = false "
			+ "AND d.eventDate BETWEEN :startDate AND :endDate")
	List<DateNote> findByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate, Pageable pageable);

	@Query("""
			  SELECT DISTINCT d
			  FROM DateNote d
			  JOIN FETCH d.notes n
			  LEFT JOIN FETCH n.personalizedNote p
			  WHERE n.user.id    = :userId
			    AND n.archived   = FALSE
			    AND d.eventDate BETWEEN :startDate AND :endDate
			""")
	List<DateNote> findWithNotesAndPersonalized(@Param("userId") Long userId,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query("""
			SELECT DISTINCT d
			  FROM DateNote d
			  JOIN FETCH d.notes n
			 WHERE n.user.id  = :userId
			   AND n.archived = TRUE
			   AND d.eventDate BETWEEN :startDate AND :endDate
			""")
	List<DateNote> findArchivedByUserAndDateRange(@Param("userId") Long userId,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query("""
			SELECT DISTINCT d
			  FROM DateNote d
			  JOIN FETCH d.notes n
			  LEFT JOIN FETCH n.personalizedNote p
			 WHERE d.id         = :dateNoteId
			   AND n.archived   = FALSE
			   AND n.user.id    = :userId
			""")
	DateNote findByIdAndUnarchivedNotes(@Param("dateNoteId") Long dateNoteId, @Param("userId") Long userId);


	Optional<DateNote> findFirstByEventDate(LocalDateTime dateNote);
}
