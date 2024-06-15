package com.project.taskManagement.repositories;

import com.project.taskManagement.dto.TaskDTO;
import com.project.taskManagement.entities.Task;
import com.project.taskManagement.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    Long countByTaskStatus(TaskStatus status);

    List<Task> findAllByTitleContaining(String title);

    List<Task> findAllByUserId(Long id);

    Long countByTaskStatusAndUserId(TaskStatus taskStatus, Long id);

    List<Task> findAllByTaskStatus(TaskStatus statusGenerated);

    List<Task> findAllByTaskStatusAndUserId(TaskStatus statusGenerated, Long id);

    Long countTaskByUserId(Long id);

}
