package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.model.Role;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.repository.ChannelRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.RoleRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;
import com.HowTo.spring_boot_HowTo.service.ChannelServiceI;

@Service
public class ChannelService implements ChannelServiceI{

	@Autowired
	ChannelRepositoryI channelRepository;
	@Autowired
	UserRepositoryI userRepository;
	@Autowired
	RoleRepositoryI roleRepository;
	
	@Override
	public List<Channel> getAllChannels() {
		// TODO Auto-generated method stub
		return channelRepository.findAll();
	}
	
	//gets pagination of all channels
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
	
	//saves channel and adds the creator role to the user
	@Override
	public Channel saveChannel(Channel channel, Long userId) {
		// TODO Auto-generated method stub
		Channel c =  channelRepository.save(channel);
		User user = userRepository.findById(userId).get();
		List<Role> liste = user.getRoles();
		liste.add(roleRepository.findByDescription("CREATOR"));
		user.setRoles(liste);
		userRepository.save(user);
		return c;
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
	
	//deletes channel and removes creator role
	@Override
	public void delete(Channel channel) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(channel.getChannelId()).get();
		List<Role> liste = user.getRoles();
		liste.remove(roleRepository.findByDescription("CREATOR"));
		List<User> users = channel.getSubscribedFromUserList();
		for(User u : users) {
			u.removeSubscription(channel);
			userRepository.save(u);
		}		
		user.setRoles(liste);
		userRepository.save(user);
		channelRepository.delete(channel);
	}
	
	//adds the user to a subscriber list
	@Override
	public Channel subscribeChannel(Channel channel, Long userId) {
		User user = userRepository.findById(userId).get();
		Channel c = channelRepository.findById(channel.getChannelId()).get();
		List<User> subscribedBy = c.getSubscribedFromUserList();
		List<Channel> subscribed = user.getSubscribedChannels();
		
		if(user != null && c != null && userId != null) {
			if(!subscribedBy.contains(user) && !subscribed.contains(c)) {
				user.addSubscription(c);
				c.addSubscribedFromUser(user);
				userRepository.save(user);
			}
		}
		return channel;
	}
	
	//removes user from subscriber list
	@Override
	public Channel unsubscribeChannel(Channel channel, Long userId) {
		User user = userRepository.findById(userId).get();
		Channel c = channelRepository.findById(channel.getChannelId()).get();
		List<User> subscribedBy = c.getSubscribedFromUserList();
		List<Channel> subscribed = user.getSubscribedChannels();
		
		if(user != null && c != null && userId != null) {
			if(subscribedBy.contains(user) && subscribed.contains(c)) {
				user.removeSubscription(c);
				c.removeSubscribedFromUser(user);
				userRepository.save(user);
			}
		}
		return channel;
	}

}
