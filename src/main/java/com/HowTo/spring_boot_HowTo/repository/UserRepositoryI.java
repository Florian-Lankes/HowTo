package com.HowTo.spring_boot_HowTo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HowTo.spring_boot_HowTo.model.User;

public interface UserRepositoryI extends JpaRepository<User, Long>{


	Optional<User> findUserByUsername(String Username); 
}
