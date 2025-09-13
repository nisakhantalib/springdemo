package com.taskmanagement.repository;

import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.User;
import com.taskmanagement.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByAssignedUser(User user);
    List<Task> findByAssignedUserId(Long userId);
    List<Task> findByStatusOrderByCreatedAtDesc(TaskStatus status);
}
