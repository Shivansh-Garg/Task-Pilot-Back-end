package com.project.taskManagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.taskManagement.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String commentText;

    private Date createdAt;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Employees user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "task_id", nullable = false)
    @JsonIgnore
    private Task task;

    public CommentDTO getCommentDTO(){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(id);
        commentDTO.setCommentText(commentText);
        commentDTO.setCreatedAt(createdAt);
        commentDTO.setPostedBy(user.getName());
        commentDTO.setTaskId(task.getId());
        return commentDTO;
    }

}
