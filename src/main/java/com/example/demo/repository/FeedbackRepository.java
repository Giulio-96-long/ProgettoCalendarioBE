package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Feedback;
import com.example.demo.entity.User;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
	
	// Inbox generale (ordinata per data)
    List<Feedback> findByReceiverOrderByCreatedAtDesc(User receiver);

    // Tutti i root (parent == null), per listaggio globale
    List<Feedback> findAllByParentIsNullOrderByCreatedAtDesc();

    // Risposte di un dato feedback
    Optional<Feedback> findFirstByParentOrderByCreatedAtAsc(Feedback parent);

    // Thread creati dall’utente
    List<Feedback> findBySenderAndParentIsNullOrderByCreatedAtDesc(User sender);
   
    List<Feedback> findByReceiverAndReadFalseOrderByCreatedAtDesc(User receiver);
    
    long countByReceiverAndReadFalse(User receiver);
    
    // Conteggio root letti senza risposta (per admin)
    @Query("SELECT COUNT(f) FROM Feedback f "
         + "WHERE f.receiver = :admin "
         + "  AND f.parent IS NULL "
         + "  AND f.read = true "
         + "  AND NOT EXISTS (SELECT r FROM Feedback r WHERE r.parent = f)")
    long countRootReadWithoutReplies(@Param("admin") User admin);
}