package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.shareDto.ShareRequestDto;
import com.example.demo.entity.Note;
import com.example.demo.entity.Share;
import com.example.demo.entity.ShareMember;
import com.example.demo.entity.User;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.ShareMemberRepository;
import com.example.demo.repository.ShareRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Iservice.ErrorLogService;
import com.example.demo.service.Iservice.ShareService;
import com.example.demo.util.AuthUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ShareServiceImpl implements ShareService {

	private final NoteRepository noteRepository;
	private final ShareRepository shareRepository;
	private final ShareMemberRepository shareMemberRepository;
	private final UserRepository userRepository;
	private final AuthUtils authUtils;
	private final ErrorLogService errorLogService;

	public ShareServiceImpl(UserRepository userRepository, ShareRepository shareRepository,
			ShareMemberRepository shareMemberRepository, NoteRepository noteRepository, AuthUtils authUtils,
			AuthUtils authUtils2, ErrorLogService errorLogService) {
		this.noteRepository = noteRepository;
		this.shareRepository = shareRepository;
		this.shareMemberRepository = shareMemberRepository;
		this.userRepository = userRepository;
		this.authUtils = authUtils;
		this.errorLogService = errorLogService;
	}

	@Override
	@Transactional
	public void shareNote(Long noteId, List<ShareRequestDto> recipients) {

		User currentUser = authUtils.getLoggedUser();
		if (currentUser == null) {
			throw new RuntimeException("User not authenticated");
		}	
		
		
		Note note = noteRepository.findById(noteId).orElseThrow(() 
				-> new EntityNotFoundException("Nota non trovata"));	
		
		if (note.getUser().getId() != currentUser.getId()) {
			throw new SecurityException("Solo il proprietario può aggiungere la condivisione");
		}

		Share share = new Share();
		share.setNote(note);
		share.setSharedBy(currentUser);	
		
		for (ShareRequestDto r : recipients) {
			try {
				User recipient = userRepository.findById(r.getUserId()).orElseThrow(
						() -> new EntityNotFoundException("Utente destinatario non trovato: " + r.getUserId()));
				ShareMember member = new ShareMember();
				member.setShare(share);
				member.setUser(recipient);			
				
				share.getMembers().add(member);
			} catch (Exception e) {
				errorLogService.logError("newNote: save history CREATE", e);
			}
			
		}
		
		shareRepository.save(share);
	}


	@Override
	public void removeForMe(Long noteId) {
		User currentUser = authUtils.getLoggedUser();
		if (currentUser == null) {
			throw new RuntimeException("User not authenticated");
		}
		
		List<ShareMember> members = shareMemberRepository
				.findByShareNoteIdAndUserId(noteId, currentUser.getId());
		for (ShareMember m : members) {
			
			m.setRemovedForMe(true);
			shareMemberRepository.save(m);
		}
	}	
	

	@Override
	public void revokeShare(Long noteId, Long recipientUserId) {
		User currentUser = authUtils.getLoggedUser();
		if (currentUser == null) {
			throw new RuntimeException("User not authenticated");
		}
		
		Note note = noteRepository.findById(noteId).orElseThrow(() 
				-> new EntityNotFoundException("Nota non trovata"));
		
		if (note.getUser().getId() != currentUser.getId()) {
			throw new SecurityException("Solo il proprietario può revocare la condivisione");
		}

		shareMemberRepository.findByShareNoteIdAndUserId(noteId, recipientUserId)
				.forEach(shareMemberRepository::delete);
	}

}
