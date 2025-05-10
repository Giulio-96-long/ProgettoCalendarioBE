package com.example.demo.service.Iservice;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryFilterDto;
import com.example.demo.dto.NoteChangeHistoryDto.NoteChangeHistoryResponseDto;
import com.example.demo.entity.Note;
import com.example.demo.entity.User;

public interface IChangeHistoryService {

	void saveChange(Note note, String changeType, User modifiedBy,
			LocalDateTime modificationDate);

	Page<NoteChangeHistoryResponseDto> getAllAndFilter(NoteChangeHistoryFilterDto filter);

}
