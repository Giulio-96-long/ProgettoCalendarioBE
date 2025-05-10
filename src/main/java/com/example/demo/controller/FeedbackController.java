package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.FeedbackDto.FeedbackResponseDto;
import com.example.demo.dto.FeedbackDto.NewCommentRequestDto;
import com.example.demo.service.FeedbackService;
import com.example.demo.service.Iservice.IErrorLogService;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

	private final FeedbackService feedbackService;
	private final IErrorLogService logErrorService;

	public FeedbackController(FeedbackService feedbackService, IErrorLogService logErrorService) {
		this.feedbackService = feedbackService;
		this.logErrorService = logErrorService;
	}
	 
    @PostMapping("/comment")
    public ResponseEntity<?> postComment(@RequestBody NewCommentRequestDto dto) {
        try {
        	boolean success = feedbackService.userSendContact(dto);
            return ResponseEntity.ok(success);
        } catch (Exception e) {
			logErrorService.logError("feedback/comment", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
    
    }

    @PostMapping("/{id}/reply")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> reply(@PathVariable Long id,
                                         @RequestBody NewCommentRequestDto dto) {
        try {
        	boolean success = feedbackService.adminReply(id, dto);
            return ResponseEntity.ok(success);
        } catch (Exception e) {
			logErrorService.logError("feedback/reply", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
    	
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<?> markRead(@PathVariable Long id) {
    	try {
    		feedbackService.markAsRead(id);
            return ResponseEntity.ok().build();
    	 } catch (Exception e) {
 			logErrorService.logError("feedback/read", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
        
    }

    @GetMapping("/unread")
    public ResponseEntity<?> getUnread() {
    	try {
    		 List<FeedbackResponseDto> list = feedbackService.getUnreadForCurrentUser();
    	        return ResponseEntity.ok(list);
    	 } catch (Exception e) {
 			logErrorService.logError("feedback/unread", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
       
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll() {
    	try {
    		  List<FeedbackResponseDto> all = feedbackService.getAllComments();
    	        return ResponseEntity.ok(all);
    	 } catch (Exception e) {
 			logErrorService.logError("feedback/all", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
      
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMy() {
    	try {
    	      List<FeedbackResponseDto> mine = feedbackService.getMyCommentsWithReplies();
    	        return ResponseEntity.ok(mine);
    	 } catch (Exception e) {
 			logErrorService.logError("feedback//my", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
  
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
        	FeedbackResponseDto dto = feedbackService.getCommentById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
			logErrorService.logError("feedback/getbyId", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
    	
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<?> getMyById(@PathVariable Long id) {
        try {
        	FeedbackResponseDto dto = feedbackService.getMyCommentById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
			logErrorService.logError("feedback/getbyId", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
    	
    }    
 
    @GetMapping("/user/unread/count")
    public ResponseEntity<?> getUnreadCountForUser() {
    	try {
    		 long count = feedbackService.countUnreadForCurrentUser();
    	        return ResponseEntity.ok(count);    
    } catch (Exception e) {
		logErrorService.logError("feedback/user/unread/count", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
	}
}

    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/reply/count")
    public ResponseEntity<?> getPendingRepliesForAdmin() {
    	try {
    		  long count = feedbackService.countFeedbacksToReply();
    	        return ResponseEntity.ok(count);
    	 } catch (Exception e) {
 			logErrorService.logError("feedback/admin/reply/count", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
      
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all-with-replies")
    public ResponseEntity<?> getAllWithReplies() {
    	try {
    		  List<FeedbackResponseDto> list = feedbackService.getAllComments();       
    	        return ResponseEntity.ok(list);
    	 } catch (Exception e) {
 			logErrorService.logError("feedback/admin/all-with-replies", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
      
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/unread/count")
    public ResponseEntity<?> getAdminUnreadCount() {
    	try {
    		long count = feedbackService.countUnreadForCurrentUser();
            return ResponseEntity.ok(count);
    	 } catch (Exception e) {
 			logErrorService.logError("feedback/getbyId", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
        
    }
}