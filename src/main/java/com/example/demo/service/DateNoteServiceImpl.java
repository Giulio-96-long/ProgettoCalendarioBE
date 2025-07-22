package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PersonalizedNoteDto.PersonalizedNoteResponseDto;
import com.example.demo.dto.fileDto.FileResponseDto;
import com.example.demo.dto.noteDto.DateNoteDetailDto;
import com.example.demo.dto.noteDto.NoteWithFilesDto;
import com.example.demo.dto.userDto.UserResponseDto;
import com.example.demo.entity.DateNote;
import com.example.demo.entity.Note;
import com.example.demo.entity.ShareMember;
import com.example.demo.entity.User;
import com.example.demo.repository.DateNoteRepository;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.ShareMemberRepository;
import com.example.demo.service.Iservice.NoteChangeHistoryService;
import com.example.demo.util.AuthUtils;
import com.example.demo.service.Iservice.DateNoteService;
import com.example.demo.service.Iservice.ErrorLogService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class DateNoteServiceImpl implements DateNoteService {

	private final DateNoteRepository dateNoteRepository;
	private final NoteChangeHistoryService noteChangeHistoryService;
	private final AuthUtils authUtils;
	private final ErrorLogService errorLogService;
	private final ShareMemberRepository shareMemberRepository;
	private final NoteRepository noteRepository;

	DateNoteServiceImpl(DateNoteRepository dateNoteRepository, NoteChangeHistoryService noteChangeHistoryService,
			AuthUtils authUtils, ErrorLogService errorLogService, ShareMemberRepository shareMemberRepository,
			NoteRepository noteRepository) {
		this.dateNoteRepository = dateNoteRepository;
		this.noteChangeHistoryService = noteChangeHistoryService;
		this.authUtils = authUtils;
		this.errorLogService = errorLogService;
		this.shareMemberRepository = shareMemberRepository;
		this.noteRepository = noteRepository;

	}

	@Override
	public DateNoteDetailDto getNotesByDateNoteId(long idDateNote) {

		User currentUser = authUtils.getLoggedUser();
		if (currentUser == null) {
			throw new RuntimeException("User not authenticated");
		}
		Long userId = currentUser.getId();

		DateNote dn = dateNoteRepository.findActiveByNoteId(idDateNote);

		if (dn == null) {
			throw new EntityNotFoundException("DateNote non trovato");
		}

		List<NoteWithFilesDto> noteDtos = new ArrayList<>();
		for (Note note : dn.getNotes()) {
			
			  if (note.getIsArchived()) {
		            continue;
		        }

			boolean isOwner = note.getUser().getId() == userId;
			// membro della condivisione
			boolean isShared = shareMemberRepository.existsByShare_Note_IdAndUser_IdAndRemovedForMeFalse(note.getId(),
					userId);

			if (!isOwner && !isShared) {
				continue;
			}

			// Mappa gli allegati
			List<FileResponseDto> fileDtos = note.getFiles().stream()
					.map(f -> new FileResponseDto(f.getId(), f.getNome(), f.getBase64())).toList();

			// Mappa la nota personalizzata (se presente)
			PersonalizedNoteResponseDto pdto = note.getPersonalizedNote() != null
					? new PersonalizedNoteResponseDto(note.getPersonalizedNote())
					: null;

			 List<ShareMember> memberships = shareMemberRepository
				        .findAllByShareNoteIdAndRemovedForMeFalseAndShareNoteArchivedFalse(note.getId());

				    // A CHI ho condiviso io: io sono lo sharer (share.sharedBy)
				    List<UserResponseDto> sharedTo = memberships.stream()
				        .filter(sm -> sm.getShare().getSharedBy().getId() == userId)
				        .map(sm -> {
				            User recipient = sm.getUser();
				            return new UserResponseDto(
				                recipient.getId(),
				                recipient.getEmail(),
				                recipient.getUsername()
				            );
				        })
				        .distinct()
				        .toList();

				    // CHI mi ha condiviso: io sono il recipient (shareMember.user)
				    List<UserResponseDto> sharedBy = memberships.stream()
				        .filter(sm -> sm.getUser().getId() == userId)
				        .map(sm -> {
				            User sharer = sm.getShare().getSharedBy();
				            return new UserResponseDto(
				                sharer.getId(),
				                sharer.getEmail(),
				                sharer.getUsername()
				            );
				        })
				        .distinct()
				        .toList();

			// Aggiungo al DTO includendo il flag isOwner
			noteDtos.add(new NoteWithFilesDto(note.getId(), note.getTitle(), note.getDescription(), note.isImportant(),
					pdto, fileDtos, sharedTo, sharedBy, isOwner));
		}

		// Se non ci sono note visibili, Access Denied
		if (noteDtos.isEmpty()) {
			throw new AccessDeniedException("Non hai accesso a nessuna nota di questa giornata");
		}

		return new DateNoteDetailDto(dn.getId(), dn.getEventDate(), noteDtos);
	}

	@Override
	@Transactional
	public boolean deleteDateNoteById(long idDateNote) {
		Optional<DateNote> dateNoteOptional = dateNoteRepository.findById(idDateNote);

		if (dateNoteOptional.isEmpty()) {
			return false;
		}

		DateNote dateNote = dateNoteOptional.get();

		for (Note note : dateNote.getNotes()) {
			try {
				noteChangeHistoryService.saveChange(note, "DELETE", note.getUser(), LocalDateTime.now());
			} catch (Exception e) {
				errorLogService.logError("DELETE intero giorno", e);
			}

			dateNoteRepository.delete(dateNote);
		}

		return true;
	}
}
