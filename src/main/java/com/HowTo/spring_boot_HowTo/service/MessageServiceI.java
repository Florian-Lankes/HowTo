package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.Message;

public interface MessageServiceI {

	public Message sendMessage(Message message);
	
	public List<Message> getMessagesByGroupId(Long groupId);
}
