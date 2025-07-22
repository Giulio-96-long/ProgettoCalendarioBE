package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryDetailDto;
import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryFilterDto;
import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryResponseDto;
import com.example.demo.dto.PersonalizedNoteDto.PersonalizedNoteResponseDto;
import com.example.demo.dto.fileDto.FileResponseDto;
import com.example.demo.dto.noteDto.NoteWithFilesDto;
import com.example.demo.dto.userDto.UserResponseDto;
import com.example.demo.entity.Note;
import com.example.demo.entity.NoteChangeHistory;
import com.example.demo.entity.Share;
import com.example.demo.entity.ShareMember;
import com.example.demo.entity.User;
import com.example.demo.repository.NoteChangeHistoryRepository;
import com.example.demo.service.Iservice.NoteChangeHistoryService;
import com.example.demo.util.AuthUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class NoteChangeHistoryServiceImpl implements NoteChangeHistoryService {

	private final NoteChangeHistoryRepository noteChangeHistoryRepository;
	private final AuthUtils authUtils;

	@PersistenceContext
	private EntityManager em;

	public NoteChangeHistoryServiceImpl(NoteChangeHistoryRepository noteChangeHistoryRepository
			, AuthUtils authUtils) {
		this.noteChangeHistoryRepository = noteChangeHistoryRepository;
		this.authUtils = authUtils;
	}

	@Override
	@Transactional
	public void saveChange(Note note, String changeType, User modifiedBy, LocalDateTime modificationDate)
			throws JsonProcessingException {
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
		sort = "desc".equalsIgnoreCase(filter.getOrder()) ? sort.descending() : sort.ascending();

		Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

		LocalDateTime start = filter.getStartDate();
		LocalDateTime end = filter.getEndDate();

		boolean hasAnyFilter = (filter.getChangeType() != null && !filter.getChangeType().isBlank())
				|| (filter.getModifiedBy() != null && !filter.getModifiedBy().isBlank())
				|| (start != null && end != null);

		Page<NoteChangeHistory> pageEntity = noteChangeHistoryRepository.search(filter.getChangeType(),
				filter.getModifiedBy(), filter.getStartDate(), filter.getEndDate(), pageable);

		return pageEntity.map(n -> {
			var dto = new NoteChangeHistoryResponseDto();
			dto.setId(n.getId());
			dto.setChangeType(n.getChangeType());
			dto.setNote(n.getNote() != null ? n.getNote().getTitle() : null);
			dto.setModifiedBy(n.getModifiedBy() != null ? n.getModifiedBy().getEmail() : "-");
			dto.setModificationDate(n.getModificationDate());
			return dto;
		});
	}
	
	 @Transactional
	@Override
	public NoteChangeHistoryDetailDto getById(Long historyId) {
        User admin = authUtils.getLoggedUser();
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            throw new AccessDeniedException("Non sei ADMIN");
        }      

        NoteChangeHistory history = noteChangeHistoryRepository.findByIdWithFiles(historyId)
        	    .orElseThrow(() -> new EntityNotFoundException("Storico non trovato: " + historyId));

       
        Note note = history.getNote();              
       
        NoteWithFilesDto noteDto = new NoteWithFilesDto();
        noteDto.setId(note.getId());
        noteDto.setTitle(note.getTitle());
        noteDto.setDescription(note.getDescription());
        noteDto.setImportant(note.isImportant());

        // personalized
        if (note.getPersonalizedNote() != null) {
            PersonalizedNoteResponseDto p = new PersonalizedNoteResponseDto();
            p.setId(note.getPersonalizedNote().getId());
            p.setCustomMessage(note.getPersonalizedNote().getCustomMessage());
          
            noteDto.setPersonalizated(p);
        }

        // files
        List<FileResponseDto> files = note.getFiles()
            .stream()
            .map(f -> {
                FileResponseDto fr = new FileResponseDto();
                fr.setId(f.getId());
                fr.setNome(f.getNome());
                fr.setBase64(f.getBase64());
      
                return fr;
            })
            .toList();
        noteDto.setFiles(files);
       
        List<UserResponseDto> sharedBy = new ArrayList<>();
        List<UserResponseDto> sharedTo = new ArrayList<>();

        for (Share share : note.getShares()) {
            // chi ha condiviso
            User owner = share.getSharedBy();
            UserResponseDto ownerDto = new UserResponseDto(
                owner.getId(),
                owner.getEmail(),
                owner.getUsername()
            );
            if (sharedBy.stream().noneMatch(u -> u.getId().equals(ownerDto.getId()))) {
                sharedBy.add(ownerDto);
            }

            // chi è stato condiviso
            for (ShareMember member : share.getMembers()) {
                if (member.isRemovedForMe()) continue;
                User u = member.getUser();
                UserResponseDto toDto = new UserResponseDto(
                    u.getId(),
                    u.getEmail(),
                    u.getUsername()
                );
                if (sharedTo.stream().noneMatch(u2 -> u2.getId().equals(toDto.getId()))) {
                    sharedTo.add(toDto);
                }
            }
        }
        noteDto.setSharedBy(sharedBy);
        noteDto.setSharedTo(sharedTo);

        //Costruisco il wrapper con i dati di history + noteDto
        NoteChangeHistoryDetailDto detail = new NoteChangeHistoryDetailDto();
        detail.setHistoryId(history.getId());
        detail.setDataNote(history.getNote().getDateNote().getEventDate());  
        detail.setChangeType(history.getChangeType());
        detail.setModifiedBy(
            history.getModifiedBy() != null
              ? history.getModifiedBy().getEmail()
              : "-"
        );
        detail.setModificationDate(history.getModificationDate());
        detail.setNote(noteDto);

        return detail;
    }

}
