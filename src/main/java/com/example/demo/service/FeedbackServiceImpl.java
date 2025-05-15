package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.FeedbackDto.FeedbackResponseDto;
import com.example.demo.dto.FeedbackDto.NewCommentRequestDto;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.User;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Iservice.FeedbackService;
import com.example.demo.util.AuthUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepo;
    private final AuthUtils authUtils;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository,
                           UserRepository userRepo,
                           AuthUtils authUtils) {
        this.feedbackRepository = feedbackRepository;
        this.userRepo = userRepo;
        this.authUtils = authUtils;
    }

    @Override
    @Transactional
    public boolean userSendContact(NewCommentRequestDto dto) {
        User user = authUtils.getLoggedUser();
        if (user == null) {
            throw new AccessDeniedException("Utente non loggato");
        }

        List<User> admins = userRepo.findByRole("ADMIN");
        if (admins.isEmpty()) {
            throw new EntityNotFoundException("Nessun ADMIN configurato");
        }
       
        for (User admin : admins) {
            Feedback log = new Feedback();
            log.setSender(user);
            log.setReceiver(admin);
            log.setSubject(dto.getSubject());
            log.setBody(dto.getBody());
            log.setRead(false);
            feedbackRepository.save(log);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean adminReply(Long parentLogId, NewCommentRequestDto dto) {
        User admin = authUtils.getLoggedUser();
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            throw new AccessDeniedException("Non sei ADMIN");
        }

        Feedback parent = feedbackRepository.findById(parentLogId)
            .orElseThrow(() -> new EntityNotFoundException("Messaggio non trovato"));

        Feedback reply = new Feedback();
        reply.setSender(admin);
        reply.setReceiver(parent.getSender());
        reply.setParent(parent);
        reply.setSubject("Re: " + parent.getSubject());
        reply.setBody(dto.getBody());
        reply.setRead(false);
        feedbackRepository.save(reply);

        return true;
    }

    @Override
    @Transactional
    public void markAsRead(Long feedbackId) {
        Feedback f = feedbackRepository.findById(feedbackId)
            .orElseThrow(() -> new EntityNotFoundException("Messaggio non trovato"));
        if (!f.getRead()) {
            f.setRead(true);
            f.setReadAt(LocalDateTime.now());
            feedbackRepository.save(f);
        }
    }

    @Override
    public List<FeedbackResponseDto> getUnreadForCurrentUser() {
        User me = authUtils.getLoggedUser();
        if (me == null) {
            throw new AccessDeniedException("Utente non loggato");
        }
        return feedbackRepository
            .findByReceiverAndReadFalseOrderByCreatedAtDesc(me)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public long countUnreadForCurrentUser() {
        User me = authUtils.getLoggedUser();
        if (me == null) {
            throw new AccessDeniedException("Utente non loggato");
        }
        return feedbackRepository.countByReceiverAndReadFalse(me);
    }

    @Override
    public List<FeedbackResponseDto> getAllComments() {
    	
        return feedbackRepository
            .findAllByParentIsNullOrderByCreatedAtDesc()
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackResponseDto> getMyCommentsWithReplies() {
        User me = authUtils.getLoggedUser();
        if (me == null) {
            throw new AccessDeniedException("Utente non loggato");
        }
        return feedbackRepository
            .findBySenderAndParentIsNullOrderByCreatedAtDesc(me)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public FeedbackResponseDto getCommentById(Long id) {
        Feedback f = feedbackRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Messaggio non trovato"));
        return toDto(f);
    }

    @Override
    public FeedbackResponseDto getMyCommentById(Long id) {
        User me = authUtils.getLoggedUser();
        if (me == null) {
            throw new AccessDeniedException("Utente non loggato");
        }
        Feedback f = feedbackRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Messaggio non trovato"));
        if (f.getSender().getId() != me.getId()) {
            throw new AccessDeniedException("Non hai permessi su questo messaggio");
        }
        return toDto(f);
    }

    @Override
    public long countFeedbacksToReply() {
        User admin = authUtils.getLoggedUser();
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            throw new AccessDeniedException("Solo gli admin possono accedere a questa funzione");
        }
        return feedbackRepository.countRootReadWithoutReplies(admin);
    }


    private FeedbackResponseDto toDto(Feedback f) {
        FeedbackResponseDto dto = new FeedbackResponseDto();
        dto.setId(f.getId());
        dto.setSubject(f.getSubject());
        dto.setBody(f.getBody());
        dto.setCreatedAt(f.getCreatedAt());
        dto.setRead(f.getRead());
        dto.setReadAt(f.getReadAt());
        dto.setSenderName(f.getSender().getEmail());

        feedbackRepository.findFirstByParentOrderByCreatedAtAsc(f)
            .ifPresent(r -> {
                FeedbackResponseDto.ReplyDto rd = new FeedbackResponseDto.ReplyDto();
                rd.setId(r.getId());
                rd.setBody(r.getBody());
                rd.setCreatedAt(r.getCreatedAt());
                rd.setRead(r.getRead());
                rd.setReadAt(r.getReadAt());
                dto.setReply(rd);
                dto.setHasReply(true);
            });

        return dto;
    }

}