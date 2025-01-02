package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.Channel;

public interface ChannelServiceI {

	List<Channel> getAllChannels();
	
	Channel saveChannel(Channel channel);
	
	Channel getChannelById(Long id);
	
	Channel updateChannel(Channel channel);
	
	void delete(Channel channel);
}
