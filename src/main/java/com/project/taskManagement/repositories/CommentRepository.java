package com.project.taskManagement.repositories;

import com.project.taskManagement.entities.Comment;
import com.project.taskManagement.entities.Task;
import com.project.taskManagement.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllCommentsByTaskId(Long id);
}
