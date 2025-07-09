package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.NoteChangeHistory;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface NoteChangeHistoryRepository extends JpaRepository<NoteChangeHistory, Long> {

	@Query("""
			SELECT n
			FROM NoteChangeHistory n
			JOIN FETCH n.note
			JOIN n.modifiedBy u
			WHERE (:changeType IS NULL   OR LOWER(n.changeType) LIKE LOWER(CONCAT('%', :changeType, '%')))
			  AND (:modifiedBy IS NULL   OR LOWER(u.username)   LIKE LOWER(CONCAT('%', :modifiedBy, '%')))
			  AND (:startDate IS NULL    OR n.modificationDate >= :startDate)
			  AND (:endDate   IS NULL    OR n.modificationDate <= :endDate)
			""")
	Page<NoteChangeHistory> search(@Param("changeType") String changeType, @Param("modifiedBy") String modifiedBy,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

	  @Query("""
		      SELECT DISTINCT h
		      FROM NoteChangeHistory h
		      JOIN FETCH h.note n
		      LEFT JOIN FETCH n.files f
		      LEFT JOIN FETCH n.personalizedNote p
		      WHERE h.id = :id
		    """)
	Optional<NoteChangeHistory> findByIdWithFiles(@Param("id") Long id);

		    @Query("""
		      SELECT DISTINCT h
		      FROM NoteChangeHistory h
		      JOIN FETCH h.note n
		      LEFT JOIN FETCH n.shares s
		      LEFT JOIN FETCH s.members m
		      LEFT JOIN FETCH m.user u
		      WHERE h.id = :id
		    """)
	Optional<NoteChangeHistory> findByIdWithShares(@Param("id") Long id);
}
