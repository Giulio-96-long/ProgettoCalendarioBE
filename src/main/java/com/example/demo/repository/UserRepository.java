package com.example.demo.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.userDto.UserResponseDto;
import com.example.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String user);

	// Metodo che cerca un account tramite email
	User findByEmail(String email);

	List<User> findByRole(String role);

	boolean existsByEmail(String email);

	List<User> findAllByRoleAndIdNot(String role, Long idNot);

	@Query("""
		      SELECT new com.example.demo.dto.userDto.UserResponseDto(
		               u.id,
		               u.email,
		               u.username
		             )
		        FROM User u
		       WHERE LOWER(u.email) LIKE CONCAT('%', LOWER(:query), '%')
		         AND u.role = :role
		    """)
		    List<UserResponseDto> searchUsersByEmailAndRole(
		        @Param("query") String query,
		        @Param("role")  String role
		    );

}
