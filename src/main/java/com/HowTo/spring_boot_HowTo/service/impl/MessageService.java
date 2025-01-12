package com.HowTo.spring_boot_HowTo.service.impl;

import java.time.LocalDateTime;
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

//	@Override
//	public List<Message> getMessagesByGroupId(Long groupId) {
//		if(groupId == null) {
//			System.out.println("groupId in MessageService == null self written Do Delete later ----<><><><>");
//		}
//		Group group = groupRepository.getReferenceById(groupId);
//		return messageRepository.findByMessageGroup(group);
//	}

}
