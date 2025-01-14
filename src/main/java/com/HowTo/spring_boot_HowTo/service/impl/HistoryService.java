package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.History;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.repository.HistoryRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.TutorialRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;
import com.HowTo.spring_boot_HowTo.service.HistoryServiceI;
@Service
public class HistoryService implements HistoryServiceI{

	@Autowired
	HistoryRepositoryI historyRepository;
	
	@Autowired
	UserRepositoryI userRepository;
	
	@Autowired
	TutorialRepositoryI tutorialRepository;
	
	@Override
	public List<History> getAllHistorys() {
		// TODO Auto-generated method stub
		return historyRepository.findAll();
	}

	@Override
	public History saveHistory(History history, Long userid, Long tutorialid) {
		User user = userRepository.findById(userid).get();
        Tutorial tutorial = tutorialRepository.findById(tutorialid).get();
        List<History> historyList = user.getHistory();
        if(user != null && history != null && historyList != null && tutorial != null) {
            if(!historyList.contains(history)) {
            	history.setHistoryOwner(user);
            	history.setHistoryTutorial(tutorial);
            	tutorial.addHistory(history);
                user.addToHistory(history);
                historyRepository.save(history);
            }
        }
		return history;
		
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



}
