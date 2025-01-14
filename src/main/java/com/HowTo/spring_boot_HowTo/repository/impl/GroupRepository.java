package com.HowTo.spring_boot_HowTo.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.HowTo.spring_boot_HowTo.model.Group;
import com.HowTo.spring_boot_HowTo.repository.GroupRepositoryI;

@Repository
public interface GroupRepository extends GroupRepositoryI, PagingAndSortingRepository<Group, Long>{

	Page <Group> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
