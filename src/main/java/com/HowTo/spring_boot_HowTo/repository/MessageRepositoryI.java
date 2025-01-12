package com.HowTo.spring_boot_HowTo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.model.Message;

public interface MessageRepositoryI extends JpaRepository<Message, Long>{
	
	//List<Message> findByMessageGroup(Group messageGroup);
	List<Message> getMessagesByMessageGroup(Group messageGroup);
	
}
