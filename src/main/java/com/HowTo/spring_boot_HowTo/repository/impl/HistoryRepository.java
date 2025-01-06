package com.HowTo.spring_boot_HowTo.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.HowTo.spring_boot_HowTo.model.History;
import com.HowTo.spring_boot_HowTo.repository.HistoryRepositoryI;

@Repository
public interface HistoryRepository extends HistoryRepositoryI{

	List<History> findByUserId(Long UserId);
}
