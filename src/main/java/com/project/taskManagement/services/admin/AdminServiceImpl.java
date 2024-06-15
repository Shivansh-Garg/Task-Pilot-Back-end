package com.project.taskManagement.services.admin;

import com.project.taskManagement.dto.CommentDTO;
import com.project.taskManagement.dto.TaskDTO;
import com.project.taskManagement.dto.UserDto;
import com.project.taskManagement.entities.Comment;
import com.project.taskManagement.entities.Employees;
import com.project.taskManagement.entities.Task;
import com.project.taskManagement.enums.TaskStatus;
import com.project.taskManagement.enums.UserRole;
import com.project.taskManagement.repositories.CommentRepository;
import com.project.taskManagement.repositories.TaskRepository;
import com.project.taskManagement.repositories.UserRepository;
import com.project.taskManagement.utils.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final JwtUtils jwtUtils;

    private final CommentRepository commentRepository;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
                .map(Employees::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Optional<Employees> optionalUser = userRepository.findById(taskDTO.getEmployeeId());
        if(optionalUser.isPresent()){
            Task task = new Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setDueDate(taskDTO.getDueDate());
            task.setPriority(taskDTO.getPriority());
            task.setTaskStatus(TaskStatus.INPROGRESS);
            task.setUser(optionalUser.get());
            return taskRepository.save(task).getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getAllTaskByTaskStatus(String status) {
        TaskStatus statusGenerated = mapStringToTaskStatus(status);
        return taskRepository.findAllByTaskStatus(statusGenerated)
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        return optionalTask.map(Task::getTaskDTO).orElse(null);
    }

    @Override
    public Long getCountByStatus(TaskStatus status) {
        return taskRepository.countByTaskStatus(status);
    }



    @Override
    public Map<TaskStatus, Long> getAllTaskCountByStatus() {
        Map<TaskStatus, Long> taskCounts = new HashMap<>();
        taskCounts.put(TaskStatus.PENDING, taskRepository.countByTaskStatus(TaskStatus.PENDING));
        taskCounts.put(TaskStatus.INPROGRESS, taskRepository.countByTaskStatus(TaskStatus.INPROGRESS));
        taskCounts.put(TaskStatus.COMPLETED, taskRepository.countByTaskStatus(TaskStatus.COMPLETED));
        taskCounts.put(TaskStatus.DEFERED, taskRepository.countByTaskStatus(TaskStatus.DEFERED));
        taskCounts.put(TaskStatus.CANCELLED, taskRepository.countByTaskStatus(TaskStatus.CANCELLED));
        return taskCounts;
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Optional<Employees> optionalUser = userRepository.findById(taskDTO.getEmployeeId());
        if(optionalTask.isPresent() && optionalUser.isPresent()){
            Task existingTask = optionalTask.get();
            existingTask.setTitle(taskDTO.getTitle());
            existingTask.setDescription(taskDTO.getDescription());
            existingTask.setDueDate(taskDTO.getDueDate());
            existingTask.setPriority(taskDTO.getPriority());
            existingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(taskDTO.getTaskStatus())));
            existingTask.setUser(optionalUser.get());
            return taskRepository.save(existingTask).getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDTO> searchTaskByTitle(String title) {

        return taskRepository.findAllByTitleContaining(title)
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
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
    public Long countTaskByUserId(Long id) {
        return taskRepository.countTaskByUserId(id);
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


}
