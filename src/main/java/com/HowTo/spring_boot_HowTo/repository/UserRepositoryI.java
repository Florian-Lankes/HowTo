package com.HowTo.spring_boot_HowTo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.HowTo.spring_boot_HowTo.model.User;

public interface UserRepositoryI extends JpaRepository<User, Long>{

	Optional<User> findUserByUsername(String username); 
	//List<User> findByNameContainingIgnoreCase (String name);
	//Page <User> findAll(Pageable pageable);
	Page <User> findByUsernameContainingIgnoreCase (String username, Pageable pageable);
	
	Optional<User> findUserByEmail(String email);
}
