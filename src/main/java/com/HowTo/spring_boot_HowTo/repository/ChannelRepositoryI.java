package com.HowTo.spring_boot_HowTo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.model.User;

public interface ChannelRepositoryI extends JpaRepository<Channel, Long>{

	Page <Channel> findByChannelnameContainingIgnoreCase (String channelname, Pageable pageable);
}
