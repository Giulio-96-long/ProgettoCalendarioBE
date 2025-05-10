package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryFilterDto;
import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryResponseDto;
import com.example.demo.entity.Note;
import com.example.demo.entity.NoteChangeHistory;
import com.example.demo.entity.User;
import com.example.demo.repository.NoteChangeHistoryRepository;
import com.example.demo.service.Iservice.IChangeHistoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class NoteChangeHistoryService implements IChangeHistoryService {

	private final NoteChangeHistoryRepository noteChangeHistoryRepository;
	
	@PersistenceContext
	private EntityManager em;

	public NoteChangeHistoryService(NoteChangeHistoryRepository noteChangeHistoryRepository) {
		this.noteChangeHistoryRepository = noteChangeHistoryRepository;
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

	    Page<NoteChangeHistory> pageEntity = hasAnyFilter
	        ? noteChangeHistoryRepository.search(
	            filter.getChangeType(), 
	            filter.getModifiedBy(), 
	            start, 
	            end, 
	            pageable)
	        : noteChangeHistoryRepository.findAll(pageable);

	    return pageEntity.map(n -> {
	        var dto = new NoteChangeHistoryResponseDto();
	        dto.setId(n.getId());
	        dto.setChangeType(n.getChangeType());
	        dto.setNoteId(n.getNote() != null ? n.getNote().getId() : null);
	        dto.setModifiedBy(n.getModifiedBy() != null
	                          ? n.getModifiedBy().getUsername()
	                          : "-");
	        dto.setModificationDate(n.getModificationDate());
	        return dto;
	    });
	}

}
