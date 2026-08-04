package com.example.tasks.controller;


import com.example.tasks.dto.TaskDTO;
import com.example.tasks.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/page")
    public Page<TaskDTO> getTaskPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "mine") String view
    ) {
        return taskService.getTaskPage(page, size, sortBy, direction, view);
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/search")
    public List<TaskDTO> searchTasks(
            @RequestParam(required = false) String assignedTo,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
            @RequestParam(required = false) String status
    ) {
        return taskService.searchTasks(assignedTo, subject, dueDate, status);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
    }

    @PostMapping
    public TaskDTO addTask(@Valid @RequestBody TaskDTO task) {
        return taskService.addTask(task);
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO task) {
        return taskService.updateTask(id, task);
    }

    @PatchMapping("/{id}/status")
    public TaskDTO updateTaskStatus(@PathVariable Long id, @RequestParam String statusName) {
        return taskService.updateTaskStatus(id, statusName);
    }

    // @GetMapping
    // public List<TaskDTO> getTasks() {
    //     return taskService.getTasks();
    // }

    // @GetMapping("/due-before")
    // public List<TaskDTO> getTasksDueBefore(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    //     return taskService.getTasksDueBefore(date);
    // }

    // @GetMapping("/status/{status}")
    // public List<TaskDTO> getTasksByStatus(@PathVariable String status) {
    //     return taskService.getTasksByStatus(status);
    // }

    // @GetMapping("/count")
    // public long countTasks() {
    //     return taskService.countTasks();
    // }

    // @GetMapping("/overdue")
    // public List<TaskDTO> getOverdueTasks() {
    //     return taskService.getOverdueTasks();
    // }

    // @DeleteMapping
    // public void deleteAllTasks() {
    //     taskService.deleteAllTasks();
    // }

    // @PostMapping("/bulk")
    // public List<TaskDTO> addTasks(@Valid @RequestBody List<@Valid TaskDTO> tasks) {
    //     return taskService.addTasksFromList(tasks);
    // }

    // @GetMapping("/user/{userId}/status/{statusName}")
    // public List<TaskDTO> getTasksByUserAndStatus(
    //         @PathVariable Long userId,
    //         @PathVariable String statusName
    // ) {
    //     return taskService.getTasksByUserAndStatus(userId, statusName);
    // }

}
