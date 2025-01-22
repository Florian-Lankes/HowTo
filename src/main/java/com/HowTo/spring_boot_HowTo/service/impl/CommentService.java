package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Comment;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.repository.CommentRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.TutorialRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;
import com.HowTo.spring_boot_HowTo.service.CommentServiceI;

@Service
public class CommentService implements CommentServiceI{
	
	@Autowired
	CommentRepositoryI commentRepository;
	@Autowired
	UserRepositoryI userRepository;
	@Autowired
	TutorialRepositoryI tutorialRepository;
	
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
	public Comment saveComment(Comment comment, Long userId, Long tutorialId) {
        // TODO Auto-generated method stub
        User user = userRepository.findById(userId).get();
        Tutorial tutorial = tutorialRepository.findById(tutorialId).get();
        
        List<Comment> ownedComments = user.getOwnedComments();

        if(user != null && tutorial != null && ownedComments != null && comment != null) {
            if(!ownedComments.contains(comment)) {
            	comment.setCommentOwner(user);
                user.addOwnedComment(comment);
                comment.setCommentTutorial(tutorial);
                tutorial.addAttachedComment(comment);
                commentRepository.save(comment);
            }
        }
        return comment;
    }

	@Override
	public Comment getCommentById(Long id) {
		Optional<Comment> opComment = commentRepository.findById(id);
		return opComment.isPresent()? opComment.get(): null;
	}

	@Override
	public Comment updateComment(Comment comment) {
		Optional<Comment> opComment = commentRepository.findById(comment.getCommentId());
		if(opComment.isPresent()) {
			Comment old = opComment.get();
			comment.setCreationDate(old.getCreationDate());
			comment.setCommentOwner(old.getCommentOwner());
			comment.setCommentTutorial(old.getCommentTutorial());
			Comment local = commentRepository.save(comment);
			return local;
		}else {
			Comment local = new Comment();
			return local;
		}
	}

	@Override
	public void delete(Comment comment) {
		// TODO Auto-generated method stub
		commentRepository.delete(comment);
	}

}
