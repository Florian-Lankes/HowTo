package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Comment;
import com.HowTo.spring_boot_HowTo.repository.CommentRepositoryI;
import com.HowTo.spring_boot_HowTo.service.CommentServiceI;

@Service
public class CommentService implements CommentServiceI{
	
	@Autowired
	CommentRepositoryI commentRepository;
	
	@Override
	public Page<Comment> getAllComments(String title, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Comment> findCommentByTitle(String title) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comment saveComment(Comment comment) {
		return commentRepository.save(comment);
	}

	@Override
	public Comment getCommentById(Long id) {
		Optional<Comment> opComment = commentRepository.findById(id);
		return opComment.isPresent()? opComment.get(): null;
	}

	@Override
	public Comment updateComment(Comment comment) {
		Comment local = commentRepository.save(comment);
		return local;
	}

	@Override
	public void delete(Comment comment) {
		// TODO Auto-generated method stub
		commentRepository.delete(comment);
	}

}
