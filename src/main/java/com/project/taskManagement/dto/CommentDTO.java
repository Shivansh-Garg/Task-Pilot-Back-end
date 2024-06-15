package com.project.taskManagement.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {

    private Long id;
    private String commentText;
    private Date createdAt;
    private Long taskId;
    private Long userId;
    private String postedBy;

}
