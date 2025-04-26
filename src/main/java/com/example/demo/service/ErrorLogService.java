package com.example.demo.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.repository.ErrorLogRepository;
import com.example.demo.service.Iservice.IErrorLogService;
import com.example.demo.util.AuthUtils;

import com.example.demo.dto.logErrorDto.LogErrorResposeDto;
import com.example.demo.entity.ErrorLog;

@Service
public class ErrorLogService implements IErrorLogService {
	
	private final ErrorLogRepository errorLogRepository;
	
	
    public ErrorLogService(ErrorLogRepository errorLogRepository, AuthUtils authUtils) {
        this.errorLogRepository = errorLogRepository;	
    }

    @Override
    public void logError(String endpoint, Exception e) {
    	ErrorLog log = new ErrorLog();    	
        
        log.setEndpoint(endpoint);             
        // Troncamento messaggio errore
        String errorMessage = e.getMessage().length() > 5000 ? e.getMessage().substring(0, 4990) + "..." : e.getMessage();
        log.setErrorMessage(errorMessage);
        
        // Troncamento stack trace
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String fullStackTrace = sw.toString();
        String truncatedStackTrace = fullStackTrace.length() > 5000 
            ? fullStackTrace.substring(0, 4990) + "..."
            : fullStackTrace;
        log.setStackTrace(truncatedStackTrace);
      
        log.setTimestamp(LocalDateTime.now());
        String user = "Anonymous"; 
        // Se l'utente è autenticato, prendi il suo nome
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            user = authentication.getName();  
        }
        log.setUser(user);
        errorLogRepository.save(log);
    }
    
    @Override
    public List<LogErrorResposeDto> getAllError(){
    	var logErrors = errorLogRepository.findAll();
    	
    	if(logErrors.size() == 0)
    		 return new ArrayList<LogErrorResposeDto>(); 
    		
    	List<LogErrorResposeDto> errorResponse =  new ArrayList<>();
    	
    	for(ErrorLog log : logErrors) {
    		 
    		LogErrorResposeDto dto = new LogErrorResposeDto();
    		
    		dto.setErrorMessage(log.getErrorMessage());
    		dto.setEndpoint(log.getEndpoint());
    		dto.setStackTrace(log.getStackTrace());
    		dto.setTimestamp(log.getTimestamp());
    		
    		errorResponse.add(dto);    		
    		 
    	}
    	
    	return errorResponse;   	
    	
    }
    
}
