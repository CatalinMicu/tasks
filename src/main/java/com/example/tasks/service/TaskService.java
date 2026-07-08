package com.example.tasks.service;

import com.example.tasks.dto.TaskDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final List<TaskDTO> tasks = new ArrayList<>();


    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public TaskDTO addTask(TaskDTO task) {
        TaskDTO buildTask = buildTask(task);
        tasks.add(buildTask);
        return buildTask;
    }

    public List<TaskDTO> addTasksFromList(List<TaskDTO> tasksFromList) {
        List<TaskDTO> buildTasks = new ArrayList<>();
        for (TaskDTO task : tasksFromList) {
            buildTasks.add(buildTask(task));
        }
        tasks.addAll(buildTasks);
        return buildTasks;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteTaskById(Long id) {
        tasks.removeIf(task -> Objects.equals(task.getId(), id));
    }

    private TaskDTO buildTask(TaskDTO task) {
        return TaskDTO.builder()
                .id(task.getId())
                .content(task.getContent())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .build();
    }

    public TaskDTO updateTask(Long id, TaskDTO task) {
        TaskDTO buildTask = buildTask(task);
        for (TaskDTO t : tasks) {
            if (Objects.equals(t.getId(), id)) {
                t.setId(id);
                t.setContent(buildTask.getContent());
                t.setDueDate(buildTask.getDueDate());
                t.setStatus(buildTask.getStatus());
                return t;
            }
        }
        return buildTask;
    }

    public TaskDTO updateTaskStatus(Long id, String status) {
        for (TaskDTO task : tasks) {
            if (Objects.equals(task.getId(), id)) {
                task.setStatus(status);
                return task;
            }
        }
        return null;
    }

    public TaskDTO getTaskById(long id) {
        for (TaskDTO task : tasks) {
            if (Objects.equals(task.getId(), id)) {
                return task;
            }
        }
        return null;
    }

    public List<TaskDTO> getTasksDueBefore(LocalDateTime dueDate) {
        return tasks.stream()
                .filter(task -> task.getDueDate() != null && task.getDueDate().isBefore(dueDate))
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByStatus(String status) {
        return tasks.stream()
                .filter(task -> task.getStatus() != null && task.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public List<TaskDTO> searchTasks(String keyword) {
        String normalized = keyword == null ? "" : keyword.trim().toLowerCase();
        return tasks.stream()
                .filter(task -> task.getContent() != null && task.getContent().toLowerCase().contains(normalized))
                .collect(Collectors.toList());
    }

    public long countTasks() {
        return tasks.size();
    }

    public List<TaskDTO> getOverdueTasks() {
        LocalDateTime today = LocalDateTime.now();
        return tasks.stream()
                .filter(task -> task.getDueDate() != null && task.getDueDate().isBefore(today))
                .collect(Collectors.toList());
    }
}
