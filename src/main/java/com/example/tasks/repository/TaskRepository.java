package com.example.tasks.repository;

import com.example.tasks.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser_UserId(Long userId);

    boolean existsByStatusType_StatusTypeId(String statusTypeId);

    @Query("""
       SELECT t
       FROM Task t
       WHERE t.user.userId = :userId
       AND LOWER(t.statusType.statusName) = LOWER(:statusName)
       ORDER BY t.dueDate ASC
       """)
    List<Task> findByUserAndStatus(
            @Param("userId") Long userId,
            @Param("statusName") String statusName
    );
}
