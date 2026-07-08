package com.example.tasks.service;

import com.example.tasks.dto.TaskDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TaskService {
    private List<TaskDTO> tasks = new ArrayList<>();


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
        for (TaskDTO task :tasksFromList) {
            buildTasks.add(buildTask(task));
        }
        tasks.addAll(buildTasks);
        return buildTasks;

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
            if (t.getId() == id) {
                t.setId(buildTask.getId());
                t.setContent(buildTask.getContent());
                t.setDueDate(buildTask.getDueDate());
                t.setStatus(buildTask.getStatus());
            }
        }
        return buildTask;
    }

    public TaskDTO getTaskById(long id) {
        for (TaskDTO task : tasks) {
            if (Objects.equals(task.getId(), id)) {
                return task;
            }
        }
        return null;
    }
}
