package com.example.tasks.repository;

import com.example.tasks.domain.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCommentRepository
        extends JpaRepository<TaskComment, Long> {

    List<TaskComment>
    findAllByTask_TaskIdOrderByCreationDateAsc(Long taskId);
}