package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.model.Message;
import com.HowTo.spring_boot_HowTo.repository.GroupRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.MessageRepositoryI;
import com.HowTo.spring_boot_HowTo.service.MessageServiceI;

@Service
public class MessageService implements MessageServiceI {

	@Autowired
	private MessageRepositoryI messageRepository;
	@Autowired
	private GroupRepositoryI groupRepository;

	@Override
	public Message saveMessage(Message message) {
		//message.setTimestamp(LocalDateTime.now());
		return messageRepository.save(message);
	}

	@Override
	public List<Message> getMessagesByMessageGroup(Group messageGroup) {
		// TODO Auto-generated method stub
		return messageRepository.getMessagesByMessageGroup(messageGroup);
	}
}
