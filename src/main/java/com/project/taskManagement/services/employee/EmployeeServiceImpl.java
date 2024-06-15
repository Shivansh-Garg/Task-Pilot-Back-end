package com.project.taskManagement.services.employee;

import com.project.taskManagement.dto.CommentDTO;
import com.project.taskManagement.dto.TaskDTO;
import com.project.taskManagement.entities.Comment;
import com.project.taskManagement.entities.Employees;
import com.project.taskManagement.entities.Task;
import com.project.taskManagement.enums.TaskStatus;
import com.project.taskManagement.repositories.CommentRepository;
import com.project.taskManagement.repositories.TaskRepository;
import com.project.taskManagement.utils.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    private final TaskRepository taskRepository;

    private final JwtUtils jwtUtils;

    private final CommentRepository commentRepository;

    @Override
    public List<TaskDTO> getTaskByUserId() {
         Employees user = jwtUtils.getLoggedInUser();
         if(user != null){
             return taskRepository.findAllByUserId(user.getId())
                     .stream()
                     .sorted(Comparator.comparing(Task::getDueDate).reversed())
                     .map(Task::getTaskDTO)
                     .collect(Collectors.toList());
         }
        throw new EntityNotFoundException("User not found in the Database");
    }

    @Override
    public TaskDTO updateTask(Long id, String status) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent()){
            Task existingTask = optionalTask.get();
            existingTask.setTaskStatus(mapStringToTaskStatus(status));
            return taskRepository.save(existingTask).getTaskDTO();
        }
        throw new EntityNotFoundException("Task not found");
    }

    @Override
    public List<TaskDTO> getTasksByStatusAndLoggedInEmployeeId(String status) {
        TaskStatus statusGenerated = mapStringToTaskStatus(status);
        Employees loggedInUser = jwtUtils.getLoggedInUser();
        if (loggedInUser != null) {
            return taskRepository.findAllByTaskStatusAndUserId(statusGenerated, loggedInUser.getId())
                    .stream()
                    .sorted(Comparator.comparing(Task::getDueDate).reversed())
                    .map(Task::getTaskDTO)
                    .collect(Collectors.toList());
        }
        throw new EntityNotFoundException("User not found in the Database");
    }

    @Override
    public CommentDTO createComment(Long taskId, String content) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        Employees user = jwtUtils.getLoggedInUser();
        if((optionalTask.isPresent()) && user != null){
            Comment comment = new Comment();
            comment.setCreatedAt(new Date());
            comment.setCommentText(content);
            comment.setTask(optionalTask.get());
            comment.setUser(user);
            return commentRepository.save(comment).getCommentDTO();
        }
        throw new EntityNotFoundException("User or Task not found");
    }

    @Override
    public List<CommentDTO> getAllCommentsByTaskId(Long id) {
        return commentRepository.findAllCommentsByTaskId(id)
                .stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .map(Comment::getCommentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(Task::getTaskDTO).orElse(null);
    }


    private TaskStatus mapStringToTaskStatus(String status){
        return switch (status){
            case "PENDING" -> TaskStatus.PENDING;
            case "INPROGRESS" -> TaskStatus.INPROGRESS;
            case "COMPLETED" -> TaskStatus.COMPLETED;
            case "DEFERED" -> TaskStatus.DEFERED;
            default -> TaskStatus.CANCELLED;
        };
    }

    @Override
    public Map<TaskStatus, Long> countTasksByStatus() {
        Employees user = jwtUtils.getLoggedInUser();
        if (user != null) {
            Map<TaskStatus, Long> taskCounts = new HashMap<>();
            taskCounts.put(TaskStatus.PENDING, taskRepository.countByTaskStatusAndUserId(TaskStatus.PENDING, user.getId()));
            taskCounts.put(TaskStatus.INPROGRESS, taskRepository.countByTaskStatusAndUserId(TaskStatus.INPROGRESS, user.getId()));
            taskCounts.put(TaskStatus.COMPLETED, taskRepository.countByTaskStatusAndUserId(TaskStatus.COMPLETED, user.getId()));
            taskCounts.put(TaskStatus.DEFERED, taskRepository.countByTaskStatusAndUserId(TaskStatus.DEFERED, user.getId()));
            taskCounts.put(TaskStatus.CANCELLED, taskRepository.countByTaskStatusAndUserId(TaskStatus.CANCELLED, user.getId()));
            return taskCounts;
        }
        throw new EntityNotFoundException("User not found in the Database");
    }



}
