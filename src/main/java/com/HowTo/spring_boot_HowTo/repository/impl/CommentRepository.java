package com.HowTo.spring_boot_HowTo.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.HowTo.spring_boot_HowTo.model.Comment;
import com.HowTo.spring_boot_HowTo.repository.CommentRepositoryI;

@Repository
public interface CommentRepository extends CommentRepositoryI, PagingAndSortingRepository<Comment, Long>{

}
