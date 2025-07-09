package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.PersonalizedNoteDto.PersonalizedNoteResponseDto;
import com.example.demo.dto.fileDto.FileResponseDto;
import com.example.demo.dto.noteDto.DateNoteSummaryDto;
import com.example.demo.dto.noteDto.NoteSummaryDto;
import com.example.demo.dto.noteDto.NoteUpdateRequestDto;
import com.example.demo.dto.noteDto.NoteWithFilesDto;
import com.example.demo.dto.shareDto.ShareRequestDto;
import com.example.demo.dto.userDto.UserResponseDto;
import com.example.demo.entity.Attachment;
import com.example.demo.entity.DateNote;
import com.example.demo.entity.Note;
import com.example.demo.entity.PersonalizedNote;
import com.example.demo.entity.Share;
import com.example.demo.entity.ShareMember;
import com.example.demo.entity.User;
import com.example.demo.repository.DateNoteRepository;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.ShareMemberRepository;
import com.example.demo.repository.ShareRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Iservice.NoteChangeHistoryService;
import com.example.demo.service.Iservice.ErrorLogService;
import com.example.demo.service.Iservice.FileService;
import com.example.demo.service.Iservice.NoteService;
import com.example.demo.service.Iservice.PersonalizedNoteService;
import com.example.demo.util.AuthUtils;
import com.example.demo.util.ConvertToFileBase64;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class NoteServiceImpl implements NoteService {

	private final AuthUtils authUtils;
	private final NoteRepository noteRepository;
	private final ErrorLogService errorLogService;
	private final DateNoteRepository dateNoteRepository;
	private final NoteChangeHistoryService noteChangeHistoryService;
	private final ShareRepository shareRepository;
	private final ShareMemberRepository shareMemberRepository;
	private final UserRepository userRepository;
	private final ShareMemberRepository shareMemberRepo;

	public NoteServiceImpl(NoteRepository noteRepository, UserRepository userRepository, AuthUtils authUtils,
			DateNoteRepository dateNoteRepository, ErrorLogService errorLogService,
			NoteChangeHistoryService noteChangeHistoryService, PersonalizedNoteService personalizedNoteService,
			FileService fileService, ShareRepository shareRepository, ShareMemberRepository shareMemberRepository,
			ShareMemberRepository shareMemberRepo) {
		this.noteRepository = noteRepository;
		this.authUtils = authUtils;
		this.errorLogService = errorLogService;
		this.dateNoteRepository = dateNoteRepository;
		this.noteChangeHistoryService = noteChangeHistoryService;
		this.shareRepository = shareRepository;
		this.shareMemberRepository = shareMemberRepository;
		this.userRepository = userRepository;
		this.shareMemberRepo = shareMemberRepo;
	}

    @Override
    @Transactional
    public long newNote(String title,
                        String description,
                        boolean isImportant,
                        String color,
                        String message,
                        Long dateNoteId,
                        LocalDateTime dateNote,
                        MultipartFile[] files,
                        List<ShareRequestDto> recipients) throws IOException {

        User user = authUtils.getLoggedUser();
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }

        DateNote dn;
        
        if (dateNoteId != null) {
            dn = dateNoteRepository.findById(dateNoteId)
                    .orElseThrow(() -> new EntityNotFoundException("DateNote non trovato"));
        } else {
            Optional<DateNote> existing = dateNoteRepository.findFirstByEventDate(dateNote);
            if (existing.isPresent()) {
                dn = existing.get();
            } else {
                dn = new DateNote(dateNote);
            }
        }

        Note newNote = new Note();
        newNote.setUser(user);
        newNote.setTitle(title);
        newNote.setDescription(description);
        newNote.setImportant(isImportant);
        newNote.setDateNote(dn);
        dn.getNotes().add(newNote);

        if (StringUtils.hasText(color) && StringUtils.hasText(message)) {
            PersonalizedNote pn = new PersonalizedNote();
            pn.setColor(color);
            pn.setCustomMessage(message);
            pn.setNote(newNote);
            newNote.setPersonalizedNote(pn);
        }

        if (files != null && files.length > 0) {
            for (MultipartFile mf : files) {
                try {
                    Attachment a = ConvertToFileBase64.convertToFileEntity(mf, newNote);
                    a.setNote(newNote);
                    newNote.getFiles().add(a);
                } catch (IOException e) {
                    errorLogService.logError("newNote: errore conversione file " + mf.getOriginalFilename(), e);
                }
            }
        }

        noteRepository.save(newNote);
        noteRepository.flush();

        if (recipients != null && !recipients.isEmpty()) {
            Share share = new Share();
            share.setNote(newNote);
            share.setSharedBy(user);

            for (ShareRequestDto r : recipients) {
                try {
                    User dest = userRepository.findById(r.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("User non trovato"));
                    ShareMember m = new ShareMember(share, dest);
                    share.getMembers().add(m);
                 
                    try {
                        noteChangeHistoryService.saveChange(newNote,
                            "SHARE with " + dest.getUsername(), user, LocalDateTime.now());
                    } catch (Exception hx) {
                        errorLogService.logError("newNote: save history SHARE", hx);
                    }
                } catch (EntityNotFoundException ex) {
                    errorLogService.logError("newNote: share member", ex);
                }
            }
           
            shareRepository.save(share);
        }
      
        try {
            noteChangeHistoryService.saveChange(newNote, "CREATE", user, LocalDateTime.now());
        } catch (Exception e) {
            errorLogService.logError("newNote: save history CREATE", e);
        }

        return dn.getId();
    }

	@Override
	public List<DateNoteSummaryDto> getNotesByMonth(int month, int year, String order) {
		Long userId = authUtils.getLoggedUserId();
		if (userId == null)
			throw new RuntimeException("Utente non autenticato");

		if (month == 0)
			month = LocalDate.now().getMonthValue();
		if (year == 0)
			year = LocalDate.now().getYear();

		LocalDate firstOfMonth = LocalDate.of(year, month, 1);
		LocalDateTime start = firstOfMonth.atStartOfDay();
		LocalDateTime end = firstOfMonth.withDayOfMonth(firstOfMonth.lengthOfMonth()).atTime(23, 59, 59);

		List<DateNote> days = dateNoteRepository.findWithNotesAndPersonalized(userId, start, end);

		List<Long> sharedNoteIds = shareMemberRepository.findNoteIdsSharedWithUser(userId, start, end);

		List<Note> sharedNotes = sharedNoteIds.isEmpty() ? Collections.emptyList()
				: noteRepository.findAllById(sharedNoteIds);

		Map<LocalDate, DateNoteSummaryDto> map = new LinkedHashMap<>();

		// Prima le mie note
		for (DateNote dn : days) {
			List<NoteSummaryDto> notesDto = new ArrayList<>();
			for (Note n : dn.getNotes()) {
				String color = (n.getPersonalizedNote() != null) ? n.getPersonalizedNote().getColor() : null;
				notesDto.add(new NoteSummaryDto(n.getId(), n.getTitle(), n.isImportant(), color));
			}
			map.put(dn.getEventDate().toLocalDate(), new DateNoteSummaryDto(dn.getId(), dn.getEventDate(), notesDto));
		}

		// Poi le note condivise
		for (Note n : sharedNotes) {
			LocalDate day = n.getDateNote().getEventDate().toLocalDate();
			DateNoteSummaryDto dto = map.computeIfAbsent(day, d -> new DateNoteSummaryDto(n.getDateNote().getId(),
					n.getDateNote().getEventDate(), new ArrayList<>()));
			String color = (n.getPersonalizedNote() != null) ? n.getPersonalizedNote().getColor() : null;
			dto.getNotes().add(new NoteSummaryDto(n.getId(), n.getTitle(), n.isImportant(), color));
		}

		// Ritorno la lista ordinata per data
		return new ArrayList<>(map.values());
	}

	@Override
	public List<DateNoteSummaryDto> getNotesForMonth(int position, String order) {
		Long userId = authUtils.getLoggedUserId();
		if (userId == null)
			throw new RuntimeException("Utente non autenticato");

		LocalDate target = LocalDate.now().plusMonths(position);
		LocalDateTime start = target.withDayOfMonth(1).atStartOfDay();
		LocalDateTime end = target.withDayOfMonth(target.lengthOfMonth()).atTime(23, 59, 59);

		List<DateNote> days = dateNoteRepository.findWithNotesAndPersonalized(userId, start, end);

		List<DateNoteSummaryDto> result = new ArrayList<>();
		for (DateNote dn : days) {
			List<NoteSummaryDto> notesDto = new ArrayList<>();
			for (Note n : dn.getNotes()) {
				String color = null;
				if (n.getPersonalizedNote() != null) {
					color = n.getPersonalizedNote().getColor();
				}
				notesDto.add(new NoteSummaryDto(n.getId(), n.getTitle(), n.isImportant(), color));
			}
			result.add(new DateNoteSummaryDto(dn.getId(), dn.getEventDate(), notesDto));
		}
		return result;
	}

	@Override
	public NoteWithFilesDto getNoteById(long id) {

		// Controllo utente autenticato
		User currentUser = authUtils.getLoggedUser();
		if (currentUser == null) {
			throw new RuntimeException("User not authenticated");
		}
		Long userId = currentUser.getId();

		// Carico la nota con allegati
		Note note = noteRepository.findNoteWithFilesById(id);
		if (note == null) {
			throw new EntityNotFoundException("Nota non trovata con id: " + id);
		}

		// Controllo permessi: o proprietario o condivisa e visibile
		boolean isOwner = note.getUser().getId() == userId;
		boolean isShared = shareMemberRepository.existsByShare_Note_IdAndUser_IdAndRemovedForMeFalse(note.getId(),
				userId);

		if (!isOwner && !isShared) {
			throw new AccessDeniedException("Non hai accesso a questa nota");
		}

		// Mappo gli allegati
		List<FileResponseDto> fileDtos = note.getFiles().stream()
				.map(f -> new FileResponseDto(f.getId(), f.getNome(), f.getBase64())).toList();

		// Mappo la nota personalizzata
		PersonalizedNoteResponseDto personalizedDto = note.getPersonalizedNote() != null
				? new PersonalizedNoteResponseDto(note.getPersonalizedNote())
				: null;
		
		 List<ShareMember> memberships = shareMemberRepository
			        .findAllByShareNoteIdAndRemovedForMeFalseAndShareNoteArchivedFalse(id);

			    // 1) A CHI ho condiviso io: io sono lo sharer (share.sharedBy)
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

			    // 2) CHI mi ha condiviso: io sono il recipient (shareMember.user)
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

		return new NoteWithFilesDto(note.getId(), note.getTitle(), note.getDescription(), note.isImportant(),
				personalizedDto, fileDtos, sharedTo, sharedBy, isOwner);
	}

	@Override
	@Transactional
	public boolean updateNote(NoteUpdateRequestDto dto) {

		Note note = noteRepository.findById(dto.getIdDateNote())
				.orElseThrow(() -> new EntityNotFoundException("Nota non trovata"));

		//
		User current = authUtils.getLoggedUser();
		if (note.getUser() == null) {
			throw new AccessDeniedException("Non puoi modificare questa nota");
		}

		if (dto.getTitle() != null) {
			note.setTitle(dto.getTitle());
		}
		if (dto.getDescription() != null) {
			note.setDescription(dto.getDescription());
		}
		if (dto.getIsImportant() != null) {
			note.setImportant(dto.getIsImportant());
		}
		note.setDateModification(LocalDateTime.now());

		// PersonalizedNote: create or update
		PersonalizedNote p = note.getPersonalizedNote();
		if (p == null) {
			p = new PersonalizedNote();
			p.setNote(note);
			note.setPersonalizedNote(p);
		}
		if (dto.getColor() != null) {
			p.setColor(dto.getColor());
		}
		if (dto.getCustomMessage() != null) {
			p.setCustomMessage(dto.getCustomMessage());
		}

		// — allegati
		if (dto.getFiles() != null && dto.getFiles().length > 0) {
			var uploaded = new ArrayList<Attachment>();
			for (var mf : dto.getFiles()) {
				try {
					var fe = ConvertToFileBase64.convertToFileEntity(mf, note);
					fe.setNote(note);
					uploaded.add(fe);
				} catch (IOException e) {
					errorLogService.logError("note/update files", e);
				}
			}

			note.getFiles().addAll(uploaded);
		}	
		

		// Salvataggio e storico
		noteRepository.save(note);

		try {
			noteChangeHistoryService.saveChange(note, "UPDATE", current, LocalDateTime.now());
		} catch (Exception e) {
			errorLogService.logError("updateNote salvataggio noteChangeHistory", e);
		}

		return true;
	}

	@Transactional
	@Override
	public boolean removeNoteById(long id) {
		User user = authUtils.getLoggedUser();

		if (user == null) {
			throw new AccessDeniedException("Non puoi modificare questa nota");
		}

		Optional<Note> optionalNote = noteRepository.findById(id);

		if (optionalNote.isEmpty()) {
			throw new AccessDeniedException("Non puoi eliminare questa nota");
		}

		Note note = optionalNote.get();

		if (note.getUser().getId() != user.getId()) {
			throw new AccessDeniedException("Non puoi eliminare questa nota non autorizzato");
		}
		noteRepository.delete(note);

		noteRepository.flush();

		try {
			noteChangeHistoryService.saveChange(note, "DELETE", user, LocalDateTime.now());
		} catch (Exception e) {
			errorLogService.logError("delete nota: salvataggio noteChangeHistory", e);
		}

		return true;
	}

	@Override
	public boolean hasImportantNotesThisWeek() {
		Long userId = authUtils.getLoggedUserId();

		LocalDate today = LocalDate.now();

		LocalDate firstDayOfMonth = today.withDayOfMonth(1);

		LocalDate lastDayOfMonth = today.withDayOfMonth(1).plusMonths(1).minusDays(1);

		LocalDateTime startDateTime = firstDayOfMonth.atStartOfDay();
		LocalDateTime endDateTime = lastDayOfMonth.atTime(23, 59, 59);

		List<Note> importantNotes = noteRepository.findByUserIdAndDateBetween(userId, startDateTime, endDateTime);

		return !importantNotes.isEmpty();
	}

	@Override
	public List<DateNoteSummaryDto> getArchivedNotesByMonth(int month, int year) {
		Long userId = authUtils.getLoggedUserId();
		if (userId == null) {
			throw new RuntimeException("Utente non autenticato");
		}

		if (year == 0) {
			year = LocalDate.now().getYear();
		}

		LocalDateTime start, end;
		if (month == 0) {
			start = LocalDate.of(year, 1, 1).atStartOfDay();
			end = LocalDate.of(year, 12, 31).atTime(23, 59, 59);
		} else {
			LocalDate firstOfMonth = LocalDate.of(year, month, 1);
			start = firstOfMonth.atStartOfDay();
			end = firstOfMonth.withDayOfMonth(firstOfMonth.lengthOfMonth()).atTime(23, 59, 59);
		}

		List<DateNote> days = dateNoteRepository.findArchivedByUserAndDateRange(userId, start, end);

		List<DateNoteSummaryDto> result = new ArrayList<>();
		for (DateNote dn : days) {
			List<NoteSummaryDto> notesDto = dn.getNotes().stream().filter(Note::getIsArchived).map(n -> {
				String color = n.getPersonalizedNote() != null ? n.getPersonalizedNote().getColor() : null;
				return new NoteSummaryDto(n.getId(), n.getTitle(), n.isImportant(), color);
			}).collect(Collectors.toList());

			if (!notesDto.isEmpty()) {
				result.add(new DateNoteSummaryDto(dn.getId(), dn.getEventDate(), notesDto));
			}
		}

		return result;
	}

	@Override
	@Transactional
	public Boolean addArchived(long idNote) {
		User user = authUtils.getLoggedUser();
		if (user == null) {
			throw new RuntimeException("Utente non autenticato");
		}

		Optional<Note> noteOptional = noteRepository.findById(idNote);

		if (!noteOptional.isPresent()) {
			throw new EntityNotFoundException("Nota non trovata con id: " + idNote);
		}

		Note note = noteOptional.get();

		boolean wasArchived = Boolean.TRUE.equals(note.getIsArchived());

		note.setIsArchived(!wasArchived);

		noteRepository.save(note);

		return true;

	}
}
