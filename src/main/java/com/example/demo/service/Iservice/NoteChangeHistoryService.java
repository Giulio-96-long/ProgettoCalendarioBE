package com.example.demo.service.Iservice;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryDetailDto;
import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryFilterDto;
import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryResponseDto;
import com.example.demo.entity.Note;
import com.example.demo.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface NoteChangeHistoryService {

	void saveChange(Note note, String changeType, User modifiedBy,
			LocalDateTime modificationDate) throws JsonProcessingException;

	Page<NoteChangeHistoryResponseDto> getAllAndFilter(NoteChangeHistoryFilterDto filter);

	NoteChangeHistoryDetailDto getById(Long id);

}
