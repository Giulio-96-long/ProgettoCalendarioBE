package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PersonalizedNoteDto.PersonalizedNoteResponseDto;
import com.example.demo.entity.PersonalizedNote;
import com.example.demo.repository.PersonalizedNoteRepository;
import com.example.demo.service.Iservice.IPersonalizedNoteService;

@Service
public class PersonalizedNoteService implements IPersonalizedNoteService{

	private final PersonalizedNoteRepository personalizedNoteRepository;
	
	public PersonalizedNoteService(PersonalizedNoteRepository personalizedNoteRepository) {
		this.personalizedNoteRepository = personalizedNoteRepository;
		
	}
	
	@Override
	public PersonalizedNoteResponseDto getPersonalizedNoteByNoteId(long noteId) {
		   PersonalizedNote personalizedNote = personalizedNoteRepository.findByNoteId(noteId);
	       return new PersonalizedNoteResponseDto(personalizedNote);	
	}

}
