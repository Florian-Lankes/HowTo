package com.HowTo.spring_boot_HowTo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.HowTo.spring_boot_HowTo.model.Group;

public interface GroupRepositoryI extends JpaRepository<Group, Long>{
	
	Page <Group> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
