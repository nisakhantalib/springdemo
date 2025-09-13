package com.taskmanagement.service;

import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.User;
import com.taskmanagement.enums.TaskStatus;
import com.taskmanagement.exception.ResourceNotFoundException;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Create a new task
    public Task createTask(Task task) {
        // The assigned user is already set in the controller if provided
        return taskRepository.save(task);
    }
    
    // Get all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    // Get task by ID
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }
    
    // Update task
    public Task updateTask(Long id, Task taskDetails) {
        Task task = getTaskById(id);
        
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        
        // Update assigned user if provided, or keep existing
        if (taskDetails.getAssignedUser() != null) {
            task.setAssignedUser(taskDetails.getAssignedUser());
        }
        
        return taskRepository.save(task);
    }
    
    // Delete task
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }
    
    // Get tasks by status
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatusOrderByCreatedAtDesc(status);
    }
    
    // Assign task to user
    @Transactional
    public Task assignTaskToUser(Long taskId, Long userId) {
        Task task = getTaskById(taskId);
        
        if (userId != null) {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
            task.setAssignedUser(user);
        } else {
            // Unassign the task if userId is null
            task.setAssignedUser(null);
        }
        
        return taskRepository.save(task);
    }
    
    // Get tasks by user
    public List<Task> getTasksByUser(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        return taskRepository.findByAssignedUserId(userId);
    }
}