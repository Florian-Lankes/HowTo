package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.History;

public interface HistoryServiceI {
	
	List<History> getAllHistorys();
	
	List<History> getAllHistoryFromUser(Long userid);
	
	History saveHistory(History history);
	
	History getHistoryById(Long id);
	
	History updateHistory(History history);
	
	void delete(History history);
}
