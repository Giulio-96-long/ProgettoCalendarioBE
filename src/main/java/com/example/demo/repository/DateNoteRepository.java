package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DateNote;

@Repository
public interface DateNoteRepository extends JpaRepository<DateNote, Long> {
	
    List<DateNote> findByEventDate(LocalDate eventDate);
    
    @Query("SELECT d FROM DateNote d " +
            "JOIN d.notes n " +
            "WHERE n.user.id = :userId " +
            "AND d.eventDate BETWEEN :startDate AND :endDate")
     List<DateNote> findByUserIdAndDateRange(
         @Param("userId") Long userId,
         @Param("startDate") LocalDateTime startDate,
         @Param("endDate") LocalDateTime endDate
     );
    
    @Query("SELECT DISTINCT d FROM DateNote d " +
    	       "JOIN d.notes n " +
    	       "WHERE n.user.id = :userId " +
    	       "AND n.archived = true " +
    	       "AND YEAR(d.eventDate) = :year " +
    	       "AND (:month = 0 OR MONTH(d.eventDate) = :month)")
    	List<DateNote> findArchivedNotesByMonthAndYear(
    	    @Param("userId") Long userId,
    	    @Param("month") int month,
    	    @Param("year") int year
    	);
}
