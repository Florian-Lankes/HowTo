package com.HowTo.spring_boot_HowTo.model;

import org.springframework.stereotype.Component;

import com.HowTo.spring_boot_HowTo.service.GroupServiceI;
import com.HowTo.spring_boot_HowTo.service.MessageServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

@Component
public class MessageDTOMapper {
	
	
	
	private static MessageServiceI messageService;
	private static UserServiceI userService;
	private static GroupServiceI groupService;
	
	public MessageDTOMapper(MessageServiceI messageService, UserServiceI userService, GroupServiceI groupService) {
		MessageDTOMapper.messageService = messageService;
		MessageDTOMapper.userService = userService;
		MessageDTOMapper.groupService = groupService;
	}
	
    public static MessageDTO toMessageDTO(Message message) {
    	// messageDTO gets created from a Message. Therefore it has an id. Message is not created ServerSided
        MessageDTO dto = new MessageDTO();
        dto.setMessageId(message.getMessageId());
        dto.setContent(message.getContent());
        dto.setMessageType(message.getMessageType());
        //User
        dto.setMessageOwnerId(message.getMessageOwner().getUserId());
        dto.setUsername(message.getMessageOwner().getUsername());
        dto.setEmail(message.getMessageOwner().getEmail());
        dto.setBirthDate(message.getMessageOwner().getBirthDate());
        //Group
        dto.setMessageGroupId(message.getMessageGroup().getGroupId());
        dto.setGroupname(message.getMessageGroup().getName());
        dto.setDescription(message.getMessageGroup().getDescription());
        dto.setCreationDate(message.getMessageGroup().getCreationDate());
        return dto;
    }

    public static Message toMessage(MessageDTO messageDTO) {
    	// only new Message, because Message is newly created by Client side
        Message message = new Message();
        // message.setMessageId(messageDTO.getMessageId());
        message.setContent(messageDTO.getContent());
        message.setMessageType(messageDTO.getMessageType());
        message.setMessageOwner(userService.getUserById(messageDTO.getMessageOwnerId()));
        message.setMessageGroup(groupService.getGroupById(messageDTO.getMessageGroupId()));
        return message;
    }
}
