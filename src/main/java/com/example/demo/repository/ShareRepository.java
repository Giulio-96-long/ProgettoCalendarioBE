package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Share;

public interface ShareRepository extends JpaRepository<Share, Long> {

}