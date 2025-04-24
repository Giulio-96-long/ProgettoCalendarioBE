package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.logErrorDto.LogErrorResposeDto;
import com.example.demo.service.Iservice.IErrorLogService;

@RestController
@RequestMapping("/api/logError")
public class LogErrorController {

	private final IErrorLogService logErrorService;
	
	public LogErrorController (IErrorLogService logErrorService) {	
		this.logErrorService = logErrorService;
	}
	
	@GetMapping("/")
	public List<LogErrorResposeDto> getAllError(){
		try {
			return logErrorService.getAllError();
		} catch (Exception e) {
			logErrorService.logError("getAllError",e);
			return new ArrayList<LogErrorResposeDto>();
		}	
	}
	
}
