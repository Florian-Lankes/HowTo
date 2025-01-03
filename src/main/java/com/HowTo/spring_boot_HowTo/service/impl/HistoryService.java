package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.HowTo.spring_boot_HowTo.model.History;
import com.HowTo.spring_boot_HowTo.repository.HistoryRepositoryI;
import com.HowTo.spring_boot_HowTo.service.HistoryServiceI;

public class HistoryService implements HistoryServiceI{

	@Autowired
	HistoryRepositoryI historyRepository;
	
	@Override
	public List<History> getAllHistorys() {
		// TODO Auto-generated method stub
		return historyRepository.findAll();
	}

	@Override
	public History saveHistory(History history) {
		// TODO Auto-generated method stub
		return historyRepository.save(history);
	}

	@Override
	public History getHistoryById(Long id) {
		// TODO Auto-generated method stub
		Optional<History> opHistory = historyRepository.findById(id);
		return opHistory.isPresent()? opHistory.get():null;
	}

	@Override
	public History updateHistory(History history) {
		History local = historyRepository.save(history);
		return local;
	}

	@Override
	public void delete(History history) {
		// TODO Auto-generated method stub
		historyRepository.delete(history);
	}

	@Override
	public List<History> getAllHistoryFromUser(Long userid) {
		List<Long> ids = Collections.singletonList(userid);
		return historyRepository.findAllById(ids);
	}

}
