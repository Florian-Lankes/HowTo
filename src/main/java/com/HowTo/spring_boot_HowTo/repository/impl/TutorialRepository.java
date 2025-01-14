package com.HowTo.spring_boot_HowTo.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.repository.TutorialRepositoryI;


@Repository
public interface TutorialRepository extends TutorialRepositoryI, PagingAndSortingRepository<Tutorial, Long>{

	Page <Tutorial> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
