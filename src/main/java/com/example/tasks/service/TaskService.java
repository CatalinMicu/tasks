package com.example.tasks.service;

import com.example.tasks.dto.TaskDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        List<TaskDTO> filteredTasks = new ArrayList<>();
        for (TaskDTO task : tasks) {
            if (task.getDueDate() != null && task.getDueDate().isBefore(dueDate)) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }

    public List<TaskDTO> getTasksByStatus(String status) {
        List<TaskDTO> filteredTasks = new ArrayList<>();
        for (TaskDTO task : tasks) {
            if (task.getStatus() != null && task.getStatus().equalsIgnoreCase(status)) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }

    public List<TaskDTO> searchTasks(String keyword) {
        String normalized = keyword == null ? "" : keyword.trim().toLowerCase();
        List<TaskDTO> filteredTasks = new ArrayList<>();
        for (TaskDTO task : tasks) {
            if (task.getContent() != null && task.getContent().toLowerCase().contains(normalized)) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }

    public long countTasks() {
        return tasks.size();
    }

    public List<TaskDTO> getOverdueTasks() {
        LocalDateTime today = LocalDateTime.now();
        List<TaskDTO> overdueTasks = new ArrayList<>();
        for (TaskDTO task : tasks) {
            if (task.getDueDate() != null && task.getDueDate().isBefore(today)) {
                overdueTasks.add(task);
            }
        }
        return overdueTasks;
    }
}
