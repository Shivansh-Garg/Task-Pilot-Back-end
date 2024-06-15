package com.project.taskManagement.services.employee;

import com.project.taskManagement.dto.CommentDTO;
import com.project.taskManagement.dto.TaskDTO;
import com.project.taskManagement.enums.TaskStatus;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    List<TaskDTO> getTaskByUserId();

    TaskDTO updateTask(Long id, String status);

    public Map<TaskStatus, Long> countTasksByStatus();

    List<TaskDTO> getTasksByStatusAndLoggedInEmployeeId(String status);

    CommentDTO createComment(Long taskId, String content);

    List<CommentDTO> getAllCommentsByTaskId(Long id);

    TaskDTO getTaskById(Long id);
}
