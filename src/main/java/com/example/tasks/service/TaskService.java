package com.example.tasks.service;

import com.example.tasks.domain.StatusType;
import com.example.tasks.domain.Task;
import com.example.tasks.domain.User;
import com.example.tasks.dto.TaskDTO;
import com.example.tasks.mapper.TaskMapper;
import com.example.tasks.repository.StatusTypeRepository;
import com.example.tasks.repository.TaskRepository;
import com.example.tasks.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final StatusTypeRepository statusTypeRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public List<TaskDTO> getTasks() {
        return taskRepository.findAll().stream().map(taskMapper::toDto).toList();
    }

    public TaskDTO getTaskById(long id) {
        return taskRepository.findById(id).map(taskMapper::toDto).orElse(null);
    }

    @Transactional
    public TaskDTO addTask(TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO, findUser(taskDTO.getUserId()), findStatus(taskDTO.getStatusTypeId()));
        if (task.getCreatedBy() == null || task.getCreatedBy().isBlank()) {
            task.setCreatedBy("system");
        }
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Transactional
    public List<TaskDTO> addTasksFromList(List<TaskDTO> tasks) {
        return tasks.stream().map(this::addTask).toList();
    }

    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return null;
        }

        task.setName(taskDTO.getName());
        task.setDueDate(taskDTO.getDueDate());
        task.setStatusType(findStatus(taskDTO.getStatusTypeId()));
        task.setUser(findUser(taskDTO.getUserId()));

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Transactional
    public TaskDTO updateTaskStatus(Long id, String statusTypeId) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return null;
        }

        task.setStatusType(findStatus(statusTypeId));
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Transactional
    public void deleteTaskById(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        }
    }

    @Transactional
    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }

    public List<TaskDTO> getTasksDueBefore(LocalDateTime dueDate) {
        return getTasks().stream()
                .filter(task -> task.getDueDate() != null && task.getDueDate().isBefore(dueDate))
                .toList();
    }

    public List<TaskDTO> getTasksByStatus(String statusTypeId) {
        return getTasks().stream()
                .filter(task -> statusTypeId.equalsIgnoreCase(task.getStatusTypeId()))
                .toList();
    }

    public List<TaskDTO> searchTasks(String keyword) {
        String normalized = keyword == null ? "" : keyword.trim().toLowerCase();
        return getTasks().stream()
                .filter(task -> task.getName() != null && task.getName().toLowerCase().contains(normalized))
                .toList();
    }

    public long countTasks() {
        return taskRepository.count();
    }

    public List<TaskDTO> getOverdueTasks() {
        LocalDateTime now = LocalDateTime.now();
        return getTasks().stream()
                .filter(task -> task.getDueDate() != null && task.getDueDate().isBefore(now))
                .toList();
    }

    private StatusType findStatus(String statusTypeId) {
        return statusTypeRepository.findById(statusTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Status type not found: " + statusTypeId));
    }

    private User findUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    public List<TaskDTO> getTasksByUserAndStatus(Long userId, String statusTypeId) {
        return taskRepository.findByUserAndStatus(userId, statusTypeId)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
