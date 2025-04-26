package com.example.demo.service.Iservice;

import java.util.List;

import com.example.demo.dto.logErrorDto.LogErrorResposeDto;

public interface IErrorLogService {
	
	List<LogErrorResposeDto> getAllError();

	void logError(String request, Exception e);
}
