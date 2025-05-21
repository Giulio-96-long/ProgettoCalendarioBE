package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.FeedbackDto.FeedbackResponseDto;
import com.example.demo.dto.FeedbackDto.NewCommentRequestDto;
import com.example.demo.service.FeedbackServiceImpl;
import com.example.demo.service.Iservice.ErrorLogService;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

	private final FeedbackServiceImpl feedbackServiceImpl;
	private final ErrorLogService logErrorService;

	public FeedbackController(FeedbackServiceImpl feedbackServiceImpl, ErrorLogService logErrorService) {
		this.feedbackServiceImpl = feedbackServiceImpl;
		this.logErrorService = logErrorService;
	}
	 
    @PostMapping("/comment")
    public ResponseEntity<?> postComment(@RequestBody NewCommentRequestDto dto) {
        try {
        	boolean success = feedbackServiceImpl.userSendContact(dto);
            return ResponseEntity.ok(success);
        } catch (Exception e) {
			logErrorService.logError("feedback/comment", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
    
    }

   
    @PostMapping("/{id}/read")
    public ResponseEntity<?> markRead(@PathVariable Long id) {
    	try {
    		feedbackServiceImpl.markAsRead(id);
            return ResponseEntity.ok().build();
    	 } catch (Exception e) {
 			logErrorService.logError("feedback/read", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
        
    }

    @GetMapping("/unread")
    public ResponseEntity<?> getUnread() {
    	try {
    		 List<FeedbackResponseDto> list = feedbackServiceImpl.getUnreadForCurrentUser();
    	        return ResponseEntity.ok(list);
    	 } catch (Exception e) {
 			logErrorService.logError("feedback/unread", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
       
    }

    

    @GetMapping("/my")
    public ResponseEntity<?> getMy() {
    	try {
    	      List<FeedbackResponseDto> mine = feedbackServiceImpl.getMyCommentsWithReplies();
    	        return ResponseEntity.ok(mine);
    	 } catch (Exception e) {
 			logErrorService.logError("feedback//my", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
  
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
        	FeedbackResponseDto dto = feedbackServiceImpl.getCommentById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
			logErrorService.logError("feedback/getbyId", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
    	
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<?> getMyById(@PathVariable Long id) {
        try {
        	FeedbackResponseDto dto = feedbackServiceImpl.getMyCommentById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
			logErrorService.logError("feedback/getbyId", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
    	
    }    
 
    @GetMapping("/user/unread/count")
    public ResponseEntity<?> getUnreadCountForUser() {
    	try {
    		 long count = feedbackServiceImpl.countUnreadForCurrentUser();
    	        return ResponseEntity.ok(count);    
    } catch (Exception e) {
		logErrorService.logError("feedback/user/unread/count", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
	}
}

    // ADMIN

    @GetMapping("/admin/reply/count")
    public ResponseEntity<?> getPendingRepliesForAdmin() {
    	try {
    		  long count = feedbackServiceImpl.countFeedbacksToReply();
    	        return ResponseEntity.ok(count);
    	 } catch (Exception e) {
 			logErrorService.logError("feedback/admin/reply/count", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
      
    }
    

    @GetMapping("/admin/all-with-replies")
    public ResponseEntity<?> getAllWithReplies() {
    	try {
    		  List<FeedbackResponseDto> list = feedbackServiceImpl.getAllComments();       
    	        return ResponseEntity.ok(list);
    	 } catch (Exception e) {
 			logErrorService.logError("feedback/admin/all-with-replies", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
      
    }
    

    @GetMapping("/admin/unread/count")
    public ResponseEntity<?> getAdminUnreadCount() {
    	try {
    		long count = feedbackServiceImpl.countUnreadForCurrentUser();
            return ResponseEntity.ok(count);
    	 } catch (Exception e) {
 			logErrorService.logError("feedback/getbyId", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
        
    }
    
    @PostMapping("/{id}/reply")    
    public ResponseEntity<?> reply(@PathVariable Long id,
                                         @RequestBody NewCommentRequestDto dto) {
        try {
        	boolean success = feedbackServiceImpl.adminReply(id, dto);
            return ResponseEntity.ok(success);
        } catch (Exception e) {
			logErrorService.logError("feedback/reply", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
		}
    	
    }
    
    @GetMapping("/all")    
    public ResponseEntity<?> getAll() {
    	try {
    		  List<FeedbackResponseDto> all = feedbackServiceImpl.getAllComments();
    	        return ResponseEntity.ok(all);
    	 } catch (Exception e) {
 			logErrorService.logError("feedback/all", e);
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
 		}
      
    }

}