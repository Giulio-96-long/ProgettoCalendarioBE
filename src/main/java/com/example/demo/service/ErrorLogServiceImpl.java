package com.example.demo.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.demo.repository.ErrorLogRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Iservice.ErrorLogService;
import com.example.demo.util.AuthUtils;
import com.example.demo.dto.logErrorDto.ErrorLogFilterDto;
import com.example.demo.dto.logErrorDto.LogErrorResponseDto;
import com.example.demo.entity.ErrorLog;
import com.example.demo.entity.User;

@Service
public class ErrorLogServiceImpl implements ErrorLogService {

	private final ErrorLogRepository errorLogRepository;
	private final AuthUtils authUtils;

	public ErrorLogServiceImpl(ErrorLogRepository errorLogRepository, UserRepository userRepository, AuthUtils authUtils) {
		this.errorLogRepository = errorLogRepository;
		this.authUtils = authUtils;
	}

	@Override
	public void logError(String endpoint, Exception e) {
	
		ErrorLog log = new ErrorLog();
		log.setEndpoint(endpoint);
		log.setErrorMessage(e.getMessage()); 
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		log.setStackTrace(sw.toString()); 
		log.setTimestamp(LocalDateTime.now());
		log.setUser(authUtils.getLoggedUser());
		errorLogRepository.save(log);
	}

	@Override
	public Page<LogErrorResponseDto> getAllError(ErrorLogFilterDto filter) {
		User admin = authUtils.getLoggedUser();
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            throw new AccessDeniedException("Non sei ADMIN");
        }
		Sort sort = Sort.by(filter.getSortBy());
		sort = "desc".equalsIgnoreCase(filter.getOrder()) ? sort.descending() : sort.ascending();
		Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

		boolean hasUser = filter.getUser() != null && filter.getUser().trim().length() >= 3;
		boolean hasEndpoint = filter.getEndpoint() != null && filter.getEndpoint().trim().length() >= 3;
		boolean hasDates = filter.getStartDate() != null && filter.getEndDate() != null;

		Page<ErrorLog> logs = (hasUser || hasEndpoint || hasDates) ? errorLogRepository.searchLogs(
				hasUser ? filter.getUser().trim() : null, hasEndpoint ? filter.getEndpoint().trim() : null,
				filter.getStartDate(), filter.getEndDate(), pageable) : errorLogRepository.findAllErrorLog(pageable);

		return logs.map(log -> {
			LogErrorResponseDto dto = new LogErrorResponseDto();
			dto.setId(log.getId());
			dto.setEndpoint(log.getEndpoint());
			dto.setErrorMessage(log.getErrorMessage());
			dto.setStackTrace(log.getStackTrace());
			dto.setTimestamp(log.getTimestamp());
			dto.setUser(log.getUser() != null ? log.getUser().getEmail() : "-");
			return dto;
		});
	}

	@Override
	public LogErrorResponseDto getErrorById(Long id) {
		Optional<ErrorLog> logOptional = errorLogRepository.findById(id);
		var log = logOptional.get();

		LogErrorResponseDto dto = new LogErrorResponseDto();
		dto.setId(log.getId());
		dto.setEndpoint(log.getEndpoint());
		dto.setErrorMessage(log.getErrorMessage());
		dto.setStackTrace(log.getStackTrace());
		dto.setTimestamp(log.getTimestamp());
		dto.setUser(log.getUser() != null ? log.getUser().getEmail() : "-");
		return dto;
	}
}