package com.example.demo.exception;


import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.logErrorDto.ErrorResponseDto;
import com.example.demo.service.Iservice.ErrorLogService;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ErrorLogService errorLogService;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> notFound(EntityNotFoundException ex, HttpServletRequest req) {
        return buildError(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> badRequest(IllegalArgumentException ex, HttpServletRequest req) {
        return buildError(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler({IOException.class})
    public ResponseEntity<ErrorResponseDto> ioError(IOException ex, HttpServletRequest req) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "IO_ERROR", "Errore di I/O", req.getRequestURI());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorResponseDto> forbidden(AccessDeniedException ex, HttpServletRequest req) {
        return buildError(HttpStatus.FORBIDDEN, "FORBIDDEN", "Accesso negato", req.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> internalError(Exception ex, HttpServletRequest req) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Errore interno", req.getRequestURI());
    }
    
    @ExceptionHandler({ DataAccessException.class, SQLException.class })
    public ResponseEntity<ErrorResponseDto> handleDatabaseError(Exception ex, HttpServletRequest req) {
        String code   = "DATABASE_ERROR";
        String message = ex.getMessage();
        if (ex instanceof ConstraintViolationException) {
            code = "CONSTRAINT_VIOLATION";
            message = "Violazione di vincolo sul database";
        }
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, code, message, req.getRequestURI());
    }

    private ResponseEntity<ErrorResponseDto> buildError(HttpStatus status, String code, String message, String path) {      
        errorLogService.logError(path, new RuntimeException(code + ": " + message));
        ErrorResponseDto body = new ErrorResponseDto(
            LocalDateTime.now(), status.value(), code, message, path
        );
        return ResponseEntity.status(status).body(body);
    }
}