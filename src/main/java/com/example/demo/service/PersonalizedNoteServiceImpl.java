package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PersonalizedNoteDto.PersonalizedNoteResponseDto;
import com.example.demo.entity.PersonalizedNote;
import com.example.demo.repository.PersonalizedNoteRepository;
import com.example.demo.service.Iservice.PersonalizedNoteService;

@Service
public class PersonalizedNoteServiceImpl implements PersonalizedNoteService{

	private final PersonalizedNoteRepository personalizedNoteRepository;
	
	public PersonalizedNoteServiceImpl(PersonalizedNoteRepository personalizedNoteRepository) {
		this.personalizedNoteRepository = personalizedNoteRepository;
		
	}
	
	@Override
	public PersonalizedNoteResponseDto getPersonalizedNoteByNoteId(long noteId) {
		   PersonalizedNote personalizedNote = personalizedNoteRepository.findByNoteId(noteId);
	       return new PersonalizedNoteResponseDto(personalizedNote);	
	}

}
