package com.example.demo.service.Iservice;

import org.springframework.data.domain.Page;

import com.example.demo.dto.logErrorDto.ErrorLogFilterDto;
import com.example.demo.dto.logErrorDto.LogErrorResponseDto;

public interface ErrorLogService {
	
	void logError(String request, Exception e);

	Page<LogErrorResponseDto> getAllError(ErrorLogFilterDto filter);

	LogErrorResponseDto getErrorById(Long id);
}
