package com.example.demo.service.Iservice;

import com.example.demo.entity.Feedback;

public interface IFeedbackService{

	Feedback createFeedback(String comment);

	Feedback respondToFeedback(Long feedbackId, String responseText);

}
