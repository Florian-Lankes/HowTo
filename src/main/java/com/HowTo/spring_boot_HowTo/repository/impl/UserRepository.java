package com.HowTo.spring_boot_HowTo.repository.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;

@Repository
public interface UserRepository extends UserRepositoryI, PagingAndSortingRepository<User, Long>{

	Optional<User> findUserByUsername(String username); 
	
	Page <User> findByUsernameContainingIgnoreCase (String username, Pageable pageable);
	
	Optional<User> findUserByEmail(String email);
}
