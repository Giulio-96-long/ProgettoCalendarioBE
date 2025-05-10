package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
   
	Optional<User> findByUsername(String user);
	
	// Metodo che cerca un account tramite email
	User findByEmail(String email);
	
	List<User> findByRole(String role);
	
}
