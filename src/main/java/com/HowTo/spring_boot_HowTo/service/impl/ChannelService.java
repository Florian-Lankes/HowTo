package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.repository.ChannelRepositoryI;
import com.HowTo.spring_boot_HowTo.service.ChannelServiceI;

@Service
public class ChannelService implements ChannelServiceI{

	@Autowired
	ChannelRepositoryI channelRepository;
	
	@Override
	public Page<Channel> getAllChannels(String channelname, Pageable pageable) {
	// TODO Auto-generated method stub
	Page <Channel> pageChannel;
	if (channelname == null) {
		pageChannel = channelRepository.findAll(pageable);
	 } else {
		 pageChannel = channelRepository.findByChannelnameContainingIgnoreCase(channelname, pageable);

	 }
	return pageChannel;
	}

	@Override
	public Channel saveChannel(Channel channel) {
		// TODO Auto-generated method stub
		return channelRepository.save(channel);
	}

	@Override
	public Channel getChannelById(Long id) {
		// TODO Auto-generated method stub
		Optional<Channel> opChannel = channelRepository.findById(id);
		return opChannel.isPresent()? opChannel.get(): null;
	}

	@Override
	public Channel updateChannel(Channel channel) {
		// TODO Auto-generated method stub
		Channel local = channelRepository.save(channel);
		return local;
	}

	@Override
	public void delete(Channel channel) {
		// TODO Auto-generated method stub
		channelRepository.delete(channel);
	}

}
