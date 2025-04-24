package com.example.demo.service.Iservice;

import java.util.List;

import com.example.demo.dto.logErrorDto.LogErrorResposeDto;

public interface IErrorLogService {
	
	void logError(String endpoint, Exception e);
	
	List<LogErrorResposeDto> getAllError();
}
