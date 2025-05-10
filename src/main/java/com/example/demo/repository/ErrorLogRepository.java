package com.example.demo.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.ErrorLog;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {

	@Query("""
			SELECT e
			FROM ErrorLog e
			LEFT JOIN e.user u
			WHERE (:username IS NULL OR u.username LIKE %:username%)
			  AND (:endpoint IS NULL OR e.endpoint LIKE %:endpoint%)
			  AND (:startDate IS NULL OR e.timestamp >= :startDate)
			  AND (:endDate   IS NULL OR e.timestamp <= :endDate)
			""")
	Page<ErrorLog> searchLogs(@Param("username") String username, @Param("endpoint") String endpoint,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

	@Query("SELECT e FROM ErrorLog e")
	Page<ErrorLog> findAllErrorLog(Pageable pageable);
}
