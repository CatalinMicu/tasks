package com.example.tasks.mapper;

import com.example.tasks.domain.StatusType;
import com.example.tasks.domain.Task;
import com.example.tasks.domain.User;
import com.example.tasks.dto.TaskDTO;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDTO toDto(Task task) {
        return TaskDTO.builder()
                .taskId(task.getTaskId())
                .name(task.getName())
                .dueDate(task.getDueDate())
                .statusName(task.getStatusType() == null ? null : task.getStatusType().getStatusName())
                .userId(task.getUser() == null ? null : task.getUser().getUserId())
                .createdBy(task.getCreatedBy())
                .build();
    }

    public Task toEntity(TaskDTO taskDTO, User user, StatusType statusType) {
        return Task.builder()
                .name(taskDTO.getName())
                .dueDate(taskDTO.getDueDate())
                .createdBy(taskDTO.getCreatedBy())
                .user(user)
                .statusType(statusType)
                .build();
    }
}
