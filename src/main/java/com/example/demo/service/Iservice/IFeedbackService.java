package com.example.demo.service.Iservice;

import java.util.List;

import com.example.demo.dto.FeedbackDto.FeedbackResponseDto;
import com.example.demo.dto.FeedbackDto.NewCommentRequestDto;

public interface IFeedbackService {		

    boolean userSendContact(NewCommentRequestDto dto);

    boolean adminReply(Long parentLogId, NewCommentRequestDto dto);

    List<FeedbackResponseDto> getAllComments();
   
    List<FeedbackResponseDto> getMyCommentsWithReplies();

    FeedbackResponseDto getCommentById(Long id);

    FeedbackResponseDto getMyCommentById(Long id);

    void markAsRead(Long feedbackId);

    List<FeedbackResponseDto> getUnreadForCurrentUser();

    long countUnreadForCurrentUser();

	long countFeedbacksToReply();
}