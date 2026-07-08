package com.example.tasks.controller;


import com.example.tasks.dto.TaskDTO;
import com.example.tasks.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
    }


    @PostMapping
    public TaskDTO addTask(@RequestBody TaskDTO task) {
        return taskService.addTask(task);
    }

    @PostMapping("/bulk")
    public List<TaskDTO> addTasks(@RequestBody List<TaskDTO> tasks) {
        return taskService.addTasksFromList(tasks);
    }







}
