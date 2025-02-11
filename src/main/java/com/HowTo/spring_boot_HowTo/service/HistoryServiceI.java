package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.History;

public interface HistoryServiceI {
	
	List<History> getAllHistorys();
	
	History saveHistory(History history, Long userid, Long tutorialid);
	
	History getHistoryById(Long id);
	
	History updateHistory(History history);
	
	void delete(History history);
}

