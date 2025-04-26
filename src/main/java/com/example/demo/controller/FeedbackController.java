package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Feedback;
import com.example.demo.entity.User;
import com.example.demo.service.Iservice.IErrorLogService;
import com.example.demo.service.Iservice.IFeedbackService;
import com.example.demo.service.Iservice.IUserService;

/*@RestController
@RequestMapping("/api/feedbacks")*/
/*public class FeedbackController {

	  private final IUserService userService;
	  private final IErrorLogService errorLogService;
	  private final IFeedbackService feedbackService;
	  
	  public FeedbackController(IUserService userService
			  ,IErrorLogService errorLogService
			  ,IFeedbackService feedbackService) {
		this.userService = userService;
		this.errorLogService = errorLogService;
		this.feedbackService = feedbackService;
		  
	  }

	  @PostMapping("/submit")
	    public ResponseEntity<?> createComment(@RequestBody Map<String, String> body) {
	      try {
	    	  String comment = body.get("comment");
		      Feedback feedback = feedbackService.createFeedback(comment);
		      return ResponseEntity.ok(feedback);
		} catch (Exception e) {
			errorLogService.logError("DateNote/getById", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}  
		  
		 
	    }
	  
	    @PostMapping("/create")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<?> creatAnswer(@RequestBody String feedback) {
	    	{
	    		 Feedback feedback = feedbackService.respondToFeedback(feedback);
	            return ResponseEntity.ok(feedback);
	        }
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non autorizzato");
	    }
	    
}*/
