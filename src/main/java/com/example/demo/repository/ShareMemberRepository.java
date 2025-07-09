package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.ShareMember;

public interface ShareMemberRepository extends JpaRepository<ShareMember, Long> {

	List<ShareMember> findByShareNoteIdAndUserId(Long noteId, long id);
	
	
	@Query("""
			  select m.share.note.id
			  from ShareMember m
			  join m.share s
			  join s.note n
			  where m.user.id = :userId
			  	and m.removedForMe = false
			  	AND n.archived = FALSE
			    and n.dateNote.eventDate between :start and :end
			  """)
			List<Long> findNoteIdsSharedWithUser( @Param("userId") Long userId);
	
	List<ShareMember> findAllByShareNoteIdAndRemovedForMeFalseAndShareNoteArchivedFalse(Long noteId);

	@Query("""
			  select m.share.note.id
			  from ShareMember m
			  join m.share s
			  join s.note n
			  where m.user.id = :userId
			  	and m.removedForMe = false
			  	AND n.archived = FALSE
			    and n.dateNote.eventDate between :start and :end
			  """)
			List<Long> findNoteIdsSharedWithUser(
			  @Param("userId") Long userId,
			  @Param("start") LocalDateTime start,
			  @Param("end")   LocalDateTime end);
	boolean existsByShare_Note_IdAndUser_IdAndRemovedForMeFalse(long id, Long userId);

}