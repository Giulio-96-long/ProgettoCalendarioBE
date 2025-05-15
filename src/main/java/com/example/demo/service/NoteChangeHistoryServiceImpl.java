package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryFilterDto;
import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryResponseDto;
import com.example.demo.entity.Note;
import com.example.demo.entity.NoteChangeHistory;
import com.example.demo.entity.User;
import com.example.demo.repository.NoteChangeHistoryRepository;
import com.example.demo.service.Iservice.NoteChangeHistoryService;
import com.example.demo.util.AuthUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class NoteChangeHistoryServiceImpl implements NoteChangeHistoryService {

	private final NoteChangeHistoryRepository noteChangeHistoryRepository;
	private final AuthUtils authUtils;
	@PersistenceContext
	private EntityManager em;

	public NoteChangeHistoryServiceImpl(NoteChangeHistoryRepository noteChangeHistoryRepository, AuthUtils authUtils) {
		this.noteChangeHistoryRepository = noteChangeHistoryRepository;
		this.authUtils = authUtils;
	}

	@Override
	@Transactional
	public void saveChange(Note note, String changeType, User modifiedBy,
		LocalDateTime modificationDate) {
		NoteChangeHistory changeLog = new NoteChangeHistory();

		Note managedNote = em.getReference(Note.class, note.getId());
		changeLog.setNote(managedNote);

		changeLog.setChangeType(changeType);
		changeLog.setModifiedBy(modifiedBy);
		changeLog.setModificationDate(modificationDate != null ? modificationDate : LocalDateTime.now());

		noteChangeHistoryRepository.save(changeLog);
	}
	
	@Override
	public Page<NoteChangeHistoryResponseDto> getAllAndFilter(NoteChangeHistoryFilterDto filter) {
		User admin = authUtils.getLoggedUser();
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            throw new AccessDeniedException("Non sei ADMIN");
        }
		
		Sort sort = Sort.by(filter.getSortBy());
	    sort = "desc".equalsIgnoreCase(filter.getOrder())
	         ? sort.descending()
	         : sort.ascending();

	    Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

	    LocalDateTime start = filter.getStartDate();
	    LocalDateTime end   = filter.getEndDate();

	    boolean hasAnyFilter =
	           (filter.getChangeType() != null && !filter.getChangeType().isBlank())
	        || (filter.getModifiedBy() != null && !filter.getModifiedBy().isBlank())
	        || (start != null && end != null);

	    Page<NoteChangeHistory> pageEntity = noteChangeHistoryRepository.search(
	    	    filter.getChangeType(),
	    	    filter.getModifiedBy(),
	    	    filter.getStartDate(),
	    	    filter.getEndDate(),
	    	    pageable
	    	);

	    return pageEntity.map(n -> {
	        var dto = new NoteChangeHistoryResponseDto();
	        dto.setId(n.getId());
	        dto.setChangeType(n.getChangeType());
	        dto.setNote(n.getNote() != null ? n.getNote().getTitle() : null);
	        dto.setModifiedBy(n.getModifiedBy() != null
	                          ? n.getModifiedBy().getEmail()
	                          : "-");
	        dto.setModificationDate(n.getModificationDate());
	        return dto;
	    });
	}

}
