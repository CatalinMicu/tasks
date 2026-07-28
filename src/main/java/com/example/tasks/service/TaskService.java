package com.example.tasks.service;

import com.example.tasks.config.PermissionChecker;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final StatusTypeRepository statusTypeRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final PermissionChecker permissionChecker;

    public List<TaskDTO> getTasks() {
        requirePermission("READ");

        List<Task> tasks;
        if (permissionChecker.isAdmin()) {
            tasks = taskRepository.findAll();
        } else {
            Long userId = permissionChecker.getCurrentUser().getUserId();
            tasks = taskRepository.findAllByUser_UserId(userId);
        }

        List<TaskDTO> taskDTOs = new ArrayList<>();
        for (Task task : tasks) {
            taskDTOs.add(taskMapper.toDto(task));
        }

        return taskDTOs;
    }

    public TaskDTO getTaskById(long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return null;
        }
        requireTaskAccess(task, "READ");
        return taskMapper.toDto(task);
    }

    @Transactional
    public TaskDTO addTask(TaskDTO taskDTO) {
        requirePermission("CREATE");
        Task task = taskMapper.toEntity(taskDTO, findUser(taskDTO.getUserId()), findStatusByName(taskDTO.getStatusName()));
        if (task.getCreatedBy() == null || task.getCreatedBy().isBlank()) {
            task.setCreatedBy("system");
        }
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Transactional
    public List<TaskDTO> addTasksFromList(List<TaskDTO> tasks) {
        List<TaskDTO> savedTasks = new ArrayList<>();

        for (TaskDTO task : tasks) {
            savedTasks.add(addTask(task));
        }

        return savedTasks;
    }

    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return null;
        }
        requireTaskAccess(task, "UPDATE");

        task.setName(taskDTO.getName());
        task.setDueDate(taskDTO.getDueDate());
        task.setStatusType(findStatusByName(taskDTO.getStatusName()));
        task.setUser(findUser(taskDTO.getUserId()));

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Transactional
    public TaskDTO updateTaskStatus(Long id, String statusName) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return null;
        }
        requireTaskAccess(task, "UPDATE_STATUS");

        task.setStatusType(findStatusByName(statusName));
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Transactional
    public void deleteTaskById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return;
        }
        requireTaskAccess(task, "DELETE");
        taskRepository.delete(task);
    }

    @Transactional
    public void deleteAllTasks() {
        requirePermission("DELETE");
        if (!permissionChecker.isAdmin()) {
            throw new AccessDeniedException("Only ADMIN can delete all tasks");
        }
        taskRepository.deleteAll();
    }

    public List<TaskDTO> getTasksDueBefore(LocalDate dueDate) {
        List<TaskDTO> matchingTasks = new ArrayList<>();

        for (TaskDTO task : getTasks()) {
            if (task.getDueDate() != null &&
                    task.getDueDate().isBefore(dueDate)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }

    public List<TaskDTO> getTasksByStatus(String statusName) {
        List<TaskDTO> matchingTasks = new ArrayList<>();

        for (TaskDTO task : getTasks()) {
            if (statusName.equalsIgnoreCase(task.getStatusName())) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }

    public List<TaskDTO> searchTasks(String assignedTo, String subject, LocalDate dueDate, String status) {
        List<TaskDTO> matchingTasks = new ArrayList<>();

        for (TaskDTO task : getTasks()) {
            if (assignedTo != null && !assignedTo.isBlank()) {
                if (task.getAssignedTo() == null ||
                        !task.getAssignedTo().toLowerCase().contains(assignedTo.toLowerCase())) {
                    continue;
                }
            }

            if (subject != null && !subject.isBlank()) {
                if (task.getName() == null ||
                        !task.getName().toLowerCase().contains(subject.toLowerCase())) {
                    continue;
                }
            }

            if (dueDate != null) {
                if (task.getDueDate() == null ||
                        !task.getDueDate().equals(dueDate)) {
                    continue;
                }
            }

            if (status != null && !status.isBlank()) {
                if (task.getStatusName() == null ||
                        !task.getStatusName().equalsIgnoreCase(status)) {
                    continue;
                }
            }

            matchingTasks.add(task);
        }

        return matchingTasks;
    }

    public long countTasks() {
        return getTasks().size();
    }

    public List<TaskDTO> getOverdueTasks() {
        LocalDate now = LocalDate.now();
        List<TaskDTO> overdueTasks = new ArrayList<>();

        for (TaskDTO task : getTasks()) {
            if (task.getDueDate() != null &&
                    task.getDueDate().isBefore(now)) {
                overdueTasks.add(task);
            }
        }

        return overdueTasks;
    }

    private StatusType findStatusByName(String statusName) {
        return statusTypeRepository.findFirstByStatusNameIgnoreCase(statusName.trim())
                .orElseThrow(() -> new IllegalArgumentException("Status not found: " + statusName));
    }

    private User findUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    public List<TaskDTO> getTasksByUserAndStatus(Long userId, String statusName) {
        List<TaskDTO> matchingTasks = new ArrayList<>();

        for (TaskDTO task : getTasks()) {
            boolean sameUser = userId.equals(task.getUserId());
            boolean sameStatus =
                    statusName.equalsIgnoreCase(task.getStatusName());

            if (sameUser && sameStatus) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }

    private void requirePermission(String action) {
        if (!permissionChecker.hasPermission("TASK", action)) {
            throw new AccessDeniedException("Missing " + action + " permission on TASK");
        }
    }

    private void requireTaskAccess(Task task, String action) {
        if (!permissionChecker.canAccessTask(task, action)) {
            throw new AccessDeniedException("Access denied for this task");
        }
    }
}
