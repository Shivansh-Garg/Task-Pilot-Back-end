package com.project.taskManagement.controller.employee;

import com.project.taskManagement.dto.CommentDTO;
import com.project.taskManagement.dto.TaskDTO;
import com.project.taskManagement.services.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksByUserId(){
        return ResponseEntity.ok(employeeService.getTaskByUserId());
    }

    @GetMapping("/task/{id}/{status}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @PathVariable String status){
        TaskDTO updatedTaskDTO = employeeService.updateTask(id,status);
        if(updatedTaskDTO == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(updatedTaskDTO);
    }
    @GetMapping("/task/find/{status}")
    public ResponseEntity<List<TaskDTO>> getTaskByStatusAndId(@PathVariable String status){
        return ResponseEntity.ok(employeeService.getTasksByStatusAndLoggedInEmployeeId(status));
    }
    @GetMapping("/task/count")
    public ResponseEntity<?> getCountByTaskStatus(){
        return ResponseEntity.ok(employeeService.countTasksByStatus());
    }

    @GetMapping("/task/comment/{id}")
    public ResponseEntity<List<CommentDTO>> getCommentsById(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getAllCommentsByTaskId(id));
    }

    @PostMapping("/task/comment/{taskId}")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long taskId, @RequestParam String content){
        CommentDTO createdCommentDTO = employeeService.createComment(taskId, content);
        if(createdCommentDTO == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDTO);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getTaskById(id));
    }
}
