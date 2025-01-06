package com.HowTo.spring_boot_HowTo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HowTo.spring_boot_HowTo.model.History;

public interface HistoryRepositoryI extends JpaRepository<History, Long>{
	
	List<History> findByUserId(Long UserId);
}
