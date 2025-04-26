package com.example.demo.exception;

import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.Iservice.IErrorLogService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*") 
public class GlobalExceptionFilter extends OncePerRequestFilter {

    private final IErrorLogService errorLogService;

    public GlobalExceptionFilter(IErrorLogService errorLogService) {
        this.errorLogService = errorLogService;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 try {
	            // Continua il filtro
	            filterChain.doFilter(request, response);
	        } catch (Exception e) {
	            // In caso di errore, registra l'errore nel database
	            String endpoint = request.getRequestURI();
	            errorLogService.logError(endpoint, e);
	            // Restituisci una risposta con errore
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            response.getWriter().write("Errore imprevisto: " + e.getMessage());
	        }
	}
}
