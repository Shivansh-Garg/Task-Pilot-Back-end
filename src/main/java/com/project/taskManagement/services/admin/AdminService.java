package com.project.taskManagement.services.admin;

import com.project.taskManagement.dto.CommentDTO;
import com.project.taskManagement.dto.TaskDTO;
import com.project.taskManagement.dto.UserDto;
import com.project.taskManagement.entities.Comment;
import com.project.taskManagement.enums.TaskStatus;

import java.util.List;
import java.util.Map;

public interface AdminService {

    List<UserDto> getUsers();

    TaskDTO createTask(TaskDTO taskDTO);

    List<TaskDTO> getAllTasks();

    List<TaskDTO> getAllTaskByTaskStatus(String status);

    void deleteTask(Long id);

    TaskDTO getTaskById(Long id);

    Long getCountByStatus(TaskStatus status);


    public Map<TaskStatus, Long> getAllTaskCountByStatus();

    TaskDTO updateTask(Long id, TaskDTO taskDTO);

    List<TaskDTO> searchTaskByTitle(String title);

    CommentDTO createComment(Long taskId, String content);

    List<CommentDTO> getAllCommentsByTaskId(Long id);

    Long countTaskByUserId(Long id);

}
