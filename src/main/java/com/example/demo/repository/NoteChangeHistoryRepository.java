package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.NoteChangeHistory;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface NoteChangeHistoryRepository extends JpaRepository<NoteChangeHistory, Long> {

	@Query("""
			SELECT n
			FROM NoteChangeHistory n
			 JOIN n.modifiedBy u
			WHERE (:changeType IS NULL   OR LOWER(n.changeType) LIKE LOWER(CONCAT('%', :changeType, '%')))
			  AND (:modifiedBy IS NULL   OR LOWER(u.username)   LIKE LOWER(CONCAT('%', :modifiedBy, '%')))
			  AND (:startDate IS NULL    OR n.modificationDate >= :startDate)
			  AND (:endDate   IS NULL    OR n.modificationDate <= :endDate)
			""")
	Page<NoteChangeHistory> search(@Param("changeType") String changeType, @Param("modifiedBy") String modifiedBy,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
}
