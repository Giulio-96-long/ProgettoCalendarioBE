package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.service.Iservice.IErrorLogService;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private final IErrorLogService errorLogService;

    public GlobalExceptionHandler(IErrorLogService errorLogService) {
        this.errorLogService = errorLogService;
    }

    public static class ApiError {
        public LocalDateTime timestamp;
        public int status;
        public String error;
        public String message;
        public String path;
        public ApiError(LocalDateTime t, int s, String e, String m, String p) {
            this.timestamp = t;
            this.status = s;
            this.error = e; 
            this.message = m;
            this.path = p;
        }
    }

    private String extractPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> onNotFound(ResourceNotFoundException ex, WebRequest req) {
        String path = extractPath(req);
        errorLogService.logError(path, ex);
        ApiError body = new ApiError(LocalDateTime.now(),
                                     HttpStatus.NOT_FOUND.value(),
                                     "Not Found",
                                     ex.getMessage(),
                                     path);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> onDbError(DataAccessException ex, WebRequest req) {
        String path = extractPath(req);
        errorLogService.logError(path, ex);
        ApiError body = new ApiError(LocalDateTime.now(),
                                     HttpStatus.CONFLICT.value(),
                                     "Database Error",
                                     ex.getMessage(),
                                     path);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> onValidation(MethodArgumentNotValidException ex, WebRequest req) {
        String path = extractPath(req);
        String errors = ex.getBindingResult().getFieldErrors().stream()
                          .map(FieldError::getDefaultMessage)
                          .collect(Collectors.joining("; "));
        errorLogService.logError(path, ex);
        ApiError body = new ApiError(LocalDateTime.now(),
                                     HttpStatus.BAD_REQUEST.value(),
                                     "Validation Failed",
                                     errors,
                                     path);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> onAll(Exception ex, WebRequest req) {
        String path = extractPath(req);
        errorLogService.logError(path, ex);
        ApiError body = new ApiError(LocalDateTime.now(),
                                     HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                     "Internal Server Error",
                                     ex.getMessage(),
                                     path);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
