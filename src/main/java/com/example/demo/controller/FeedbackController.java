package com.example.demo.controller;

import java.util.List;

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

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

	private final FeedbackServiceImpl feedbackServiceImpl;	

	public FeedbackController(FeedbackServiceImpl feedbackServiceImpl) {
		this.feedbackServiceImpl = feedbackServiceImpl;	
	}

	@PostMapping("/comment")
	public ResponseEntity<?> postComment(@RequestBody NewCommentRequestDto dto) {

		boolean success = feedbackServiceImpl.userSendContact(dto);
		return ResponseEntity.ok(success);

	}

	@PostMapping("/{id}/read")
	public ResponseEntity<?> markRead(@PathVariable Long id) {

		feedbackServiceImpl.markAsRead(id);
		return ResponseEntity.ok().build();

	}

	@GetMapping("/unread")
	public ResponseEntity<?> getUnread() {

		List<FeedbackResponseDto> list = feedbackServiceImpl.getUnreadForCurrentUser();
		return ResponseEntity.ok(list);		

	}

	@GetMapping("/my")
	public ResponseEntity<?> getMy() {

		List<FeedbackResponseDto> mine = feedbackServiceImpl.getMyCommentsWithReplies();
		return ResponseEntity.ok(mine);

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {

		FeedbackResponseDto dto = feedbackServiceImpl.getCommentById(id);
		return ResponseEntity.ok(dto);

	}

	@GetMapping("/my/{id}")
	public ResponseEntity<?> getMyById(@PathVariable Long id) {

		FeedbackResponseDto dto = feedbackServiceImpl.getMyCommentById(id);
		return ResponseEntity.ok(dto);

	}

	@GetMapping("/user/unread/count")
	public ResponseEntity<?> getUnreadCountForUser() {

		long count = feedbackServiceImpl.countUnreadForCurrentUser();
		return ResponseEntity.ok(count);

	}

	// ADMIN
	@GetMapping("/admin/reply/count")
	public ResponseEntity<?> getPendingRepliesForAdmin() {

		long count = feedbackServiceImpl.countFeedbacksToReply();
		return ResponseEntity.ok(count);

	}

	@GetMapping("/admin/all-with-replies")
	public ResponseEntity<?> getAllWithReplies() {

		List<FeedbackResponseDto> list = feedbackServiceImpl.getAllComments();
		return ResponseEntity.ok(list);

	}

	@GetMapping("/admin/unread/count")
	public ResponseEntity<?> getAdminUnreadCount() {

		long count = feedbackServiceImpl.countUnreadForCurrentUser();
		return ResponseEntity.ok(count);

	}

	@PostMapping("/{id}/reply")
	public ResponseEntity<?> reply(@PathVariable Long id, @RequestBody NewCommentRequestDto dto) {

		boolean success = feedbackServiceImpl.adminReply(id, dto);
		return ResponseEntity.ok(success);

	}

	@GetMapping("/all")
	public ResponseEntity<?> getAll() {

		List<FeedbackResponseDto> all = feedbackServiceImpl.getAllComments();
		return ResponseEntity.ok(all);

	}

}