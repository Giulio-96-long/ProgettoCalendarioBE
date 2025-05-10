package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.example.demo.entity.Attachment;
import com.example.demo.entity.DateNote;
import com.example.demo.entity.Note;
import com.example.demo.entity.PersonalizedNote;
import com.example.demo.entity.User;
import com.example.demo.repository.DateNoteRepository;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Iservice.IChangeHistoryService;
import com.example.demo.service.Iservice.IErrorLogService;
import com.example.demo.service.Iservice.IFileService;
import com.example.demo.service.Iservice.INoteService;
import com.example.demo.service.Iservice.IPersonalizedNoteService;
import com.example.demo.util.AuthUtils;
import com.example.demo.util.ConvertToFileBase64;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class NoteService implements INoteService {

	private final AuthUtils authUtils;
	private final NoteRepository noteRepository;
	private final IErrorLogService errorLogService;
	private final DateNoteRepository dateNoteRepository;
	private final IChangeHistoryService changeHistoryService;

	public NoteService(NoteRepository noteRepository, UserRepository userRepository, AuthUtils authUtils,
			DateNoteRepository dateNoteRepository, IErrorLogService errorLogService,
			IChangeHistoryService changeHistoryService, IPersonalizedNoteService personalizedNoteService,
			IFileService fileService) {
		this.noteRepository = noteRepository;
		this.authUtils = authUtils;
		this.errorLogService = errorLogService;
		this.dateNoteRepository = dateNoteRepository;
		this.changeHistoryService = changeHistoryService;
	}

	@Override
	public long newNote(String title, String description, boolean isImportant, String color, String message,
			Long dateNoteId, LocalDateTime dateNote, MultipartFile[] files, String pathFile)
			throws java.io.IOException {

		User user = authUtils.getLoggedUser();
		if (user == null) {
			throw new RuntimeException("User not authenticated");
		}
		DateNote dn;
		if (dateNoteId != null) {
			// usa quello esistente
			dn = dateNoteRepository.findById(dateNoteId)
					.orElseThrow(() -> new EntityNotFoundException("DateNote non trovato"));
		} else {
			// crea uno nuovo con la data passata
			if (dateNote == null) {
				throw new IllegalArgumentException("Devi passare dateNote o dateNoteId");
			}
			dn = new DateNote();
			dn.setEventDate(dateNote);
			dateNoteRepository.save(dn);
		}

		// Crea la Note e associa la DateNote
		Note newNote = new Note();
		newNote.setUser(user);
		newNote.setTitle(title);
		newNote.setDescription(description);
		newNote.setImportant(isImportant);
		newNote.setDateNote(dn);
		dn.getNotes().add(newNote);

		if (StringUtils.hasText(color) && StringUtils.hasText(message)) {
			PersonalizedNote personalizedNote = new PersonalizedNote();
			personalizedNote.setCustomMessage(message);
			personalizedNote.setColor(color);
			personalizedNote.setNote(newNote);
			newNote.setPersonalizedNote(personalizedNote);
		}

		if (files != null && files.length > 0) {
			List<Attachment> fileEntities = new ArrayList<>();
			for (MultipartFile multipartFile : files) {
				try {
					Attachment fileEntity = ConvertToFileBase64.convertToFileEntity(multipartFile, pathFile, newNote);
					fileEntity.setNote(newNote);
					fileEntities.add(fileEntity);
				} catch (Exception e) {
					errorLogService.logError("note/searchByMonth", e);
				}
			}
			newNote.setFiles(fileEntities);
		}

		noteRepository.save(newNote);

		changeHistoryService.saveChange(newNote, "CREATE", user, LocalDateTime.now());

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

		List<DateNoteSummaryDto> result = new ArrayList<>();
		for (DateNote dn : days) {
			List<NoteSummaryDto> notesDto = new ArrayList<>();
			for (Note n : dn.getNotes()) {
				String color = null;
				if (n.getPersonalizedNote() != null) {
					color = n.getPersonalizedNote().getColor();
				}
				notesDto.add(new NoteSummaryDto(n.getTitle(), n.isImportant(), color));
			}
			result.add(new DateNoteSummaryDto(dn.getId(), dn.getEventDate(), notesDto));
		}
		return result;
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
				notesDto.add(new NoteSummaryDto(n.getTitle(), n.isImportant(), color));
			}
			result.add(new DateNoteSummaryDto(dn.getId(), dn.getEventDate(), notesDto));
		}
		return result;
	}

	@Override
	public NoteWithFilesDto getNoteById(long id) {
		Note note = noteRepository.findNoteWithFilesById(id);

		if (note == null) {
			throw new EntityNotFoundException("Nota non trovata con id: " + id);
		}

		List<FileResponseDto> fileDtos = new ArrayList<>();
		for (Attachment file : note.getFiles()) {
			fileDtos.add(new FileResponseDto(file.getId(), file.getNome(), file.getBase64()));
		}

		PersonalizedNoteResponseDto personalizedDto = null;
		if (note.getPersonalizedNote() != null) {
			personalizedDto = new PersonalizedNoteResponseDto(note.getPersonalizedNote());
		}

		return new NoteWithFilesDto(note.getId(), note.getTitle(), note.getDescription(), note.isImportant(),
				personalizedDto, fileDtos);
	}

	@Override
	@Transactional
	public boolean updateNote(NoteUpdateRequestDto dto) {
	    // 1) Carica la nota
	    Note note = noteRepository.findById(dto.getIdDateNote())
	        .orElseThrow(() -> new EntityNotFoundException("Nota non trovata"));

	    // 2) Controllo autorizzazione
	    User current = authUtils.getLoggedUser();
	    if (note.getUser() == null) {
	        throw new AccessDeniedException("Non puoi modificare questa nota");
	    }

	    // 3) Campi base
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

	    // 4) PersonalizedNote: create or update
	    PersonalizedNote p = note.getPersonalizedNote();
	    if (p == null) {
	        // prima non c’era: lo creiamo
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
					var fe = ConvertToFileBase64.convertToFileEntity(mf, dto.getPathFile(), note);
					fe.setNote(note);
					uploaded.add(fe);
				} catch (IOException e) {
					errorLogService.logError("note/update files", e);
				}
			}

			note.getFiles().addAll(uploaded);
		}

	    //Salvataggio e storico
	    noteRepository.save(note);
	    changeHistoryService.saveChange(note , "UPDATE", current, LocalDateTime.now());
	    return true;
	}


	@Transactional
	@Override
	public boolean removeNoteById(long id) {
		User user = authUtils.getLoggedUser();

		if (user == null) {
			throw new AccessDeniedException("Non puoi modificare questa nota");
		}

		// Trova la nota tramite l'id e verifica che appartenga all'utente
		Optional<Note> optionalNote = noteRepository.findById(id);

		if (optionalNote.isEmpty()) {
	        throw new AccessDeniedException("Non puoi eliminare questa nota");
		}

		Note note = optionalNote.get();

		// Verifica che la nota appartenga all'utente loggato
		if (note.getUser().getId() != user.getId()) {
	        throw new AccessDeniedException("Non puoi eliminare questa nota non autorizzato");
		}
		noteRepository.delete(note);

		noteRepository.flush();

		changeHistoryService.saveChange(note, "DELETE", user, LocalDateTime.now());

		return true;
	}

	@Override
	public boolean hasImportantNotesThisWeek() {
		Long userId = authUtils.getLoggedUserId();

		// Ottieni la data di oggi
		LocalDate today = LocalDate.now();

		// Ottieni il primo giorno del mese corrente
		LocalDate firstDayOfMonth = today.withDayOfMonth(1);

		// Ottieni l'ultimo giorno del mese corrente
		LocalDate lastDayOfMonth = today.withDayOfMonth(1).plusMonths(1).minusDays(1);

		// Convertili in LocalDateTime (per includere anche l'ora)
		LocalDateTime startDateTime = firstDayOfMonth.atStartOfDay(); // Primo giorno, inizio del giorno (00:00)
		LocalDateTime endDateTime = lastDayOfMonth.atTime(23, 59, 59); // Ultimo giorno, fine del giorno (23:59:59)

		// Query che cerca note importanti per l'utente tra startOfWeek e endOfWeek
		List<Note> importantNotes = noteRepository.findByUserIdAndDateBetween(userId, startDateTime, endDateTime);

		return !importantNotes.isEmpty();
	}

	@Override
	public List<DateNoteSummaryDto> getArchivedNotesByMonth(int month, int year) {
		Long userId = authUtils.getLoggedUserId();
		if (userId == null) {
			throw new RuntimeException("Utente non autenticato");
		}

		// se anno = 0 → anno corrente
		if (year == 0) {
			year = LocalDate.now().getYear();
		}

		LocalDateTime start;
		LocalDateTime end;

		if (month == 0) {
			// tutto l’anno
			start = LocalDate.of(year, 1, 1).atStartOfDay();
			end = LocalDate.of(year, 12, 31).atTime(23, 59, 59);
		} else {
			// solo il mese specificato
			LocalDate firstOfMonth = LocalDate.of(year, month, 1);
			start = firstOfMonth.atStartOfDay();
			end = firstOfMonth.withDayOfMonth(firstOfMonth.lengthOfMonth()).atTime(23, 59, 59);
		}

		List<DateNote> days = dateNoteRepository.findArchivedByUserAndDateRange(userId, start, end);

		// mappatura in DTO
		List<DateNoteSummaryDto> result = new ArrayList<>();
		for (DateNote dn : days) {
			List<NoteSummaryDto> notesDto = new ArrayList<>();
			for (Note n : dn.getNotes()) {
				String color = n.getPersonalizedNote() != null ? n.getPersonalizedNote().getColor() : null;
				notesDto.add(new NoteSummaryDto(n.getTitle(), n.isImportant(), color));
			}
			result.add(new DateNoteSummaryDto(dn.getId(), dn.getEventDate(), notesDto));
		}

		return result;
	}

	@Override
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

		note.setArchived(!note.getIsArchived());

		noteRepository.save(note);

		changeHistoryService.saveChange(note, "ARCHIVED", user, LocalDateTime.now());

		return true;
	}
}
