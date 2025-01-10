package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.HowTo.spring_boot_HowTo.model.Channel;

public interface ChannelServiceI {
	
	Page<Channel> getAllChannels(String channelname, Pageable pageable);
	
	Channel saveChannel(Channel channel, Long userId);
	
	Channel getChannelById(Long id);
	
	Channel updateChannel(Channel channel);
	
	Channel subscribeChannel(Channel channel, Long userId);
	
	Channel unsubscribeChannel(Channel channel, Long userId);
	
	void delete(Channel channel);
}
