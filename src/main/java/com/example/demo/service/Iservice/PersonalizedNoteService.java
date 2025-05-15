package com.example.demo.service.Iservice;

import com.example.demo.dto.PersonalizedNoteDto.PersonalizedNoteResponseDto;

public interface PersonalizedNoteService {

	PersonalizedNoteResponseDto getPersonalizedNoteByNoteId(long id);

}
