package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.HowTo.spring_boot_HowTo.model.Comment;


public interface CommentServiceI {

	Page<Comment> getAllComments(String title, Pageable pageable);
	
	List<Comment> findCommentByTitle(String title);
	
	Comment saveComment(Comment comment);
	
	Comment getCommentById(Long id);
	
	Comment updateComment(Comment comment);
	
	void delete(Comment comment);
}
