package com.HowTo.spring_boot_HowTo.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.repository.ChannelRepositoryI;

@Repository
public interface ChannelRepository extends ChannelRepositoryI, PagingAndSortingRepository<Channel, Long>{

	Page <Channel> findByChannelnameContainingIgnoreCase (String channelname, Pageable pageable);
}
