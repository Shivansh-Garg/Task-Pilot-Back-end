package com.project.taskManagement.services.comment;

import com.project.taskManagement.entities.Comment;
import com.project.taskManagement.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentInterfaceImpl implements CommentInterface{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment addComment(Comment comment) {
        return this.commentRepository.save(comment);
    }
}
