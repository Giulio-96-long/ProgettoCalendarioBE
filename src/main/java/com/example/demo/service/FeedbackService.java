package com.example.demo.service;

import com.example.demo.entity.Feedback;
import com.example.demo.entity.User;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.service.Iservice.IFeedbackService;
import com.example.demo.service.Iservice.IUserService;
import com.example.demo.util.AuthUtils;

public class FeedbackService implements IFeedbackService{

	
	private final AuthUtils authUtils;
    private FeedbackRepository feedbackRepository;
    
   
    public FeedbackService(FeedbackRepository feedbackRepository
			, IUserService userService, AuthUtils authUtils) {
		this.feedbackRepository = feedbackRepository;
		this.authUtils = authUtils;
	}
	
    @Override
	 public Feedback createFeedback(String comment) {
	        Feedback feedback = new Feedback();
	        feedback.setComment(comment);
	        return feedbackRepository.save(feedback);
	    }    

    @Override
	public Feedback respondToFeedback(Long feedbackId, String responseText) {
	        Feedback feedback = feedbackRepository.findById(feedbackId)
	                .orElseThrow(() -> new RuntimeException("Feedback non trovato"));

	        User admin = authUtils.getLoggedUser();
	        feedback.setResponse(responseText);
	        feedback.setAdmin(admin);

	        return feedbackRepository.save(feedback);
	    }	
}
