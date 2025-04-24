package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Attachment;

@Repository
public interface FileRepository extends JpaRepository<Attachment, Long> {
    // Puoi aggiungere metodi personalizzati se necessario
}