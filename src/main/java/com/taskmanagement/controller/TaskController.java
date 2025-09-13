package com.taskmanagement.controller;

import com.taskmanagement.dto.ApiResponse;
import com.taskmanagement.dto.TaskDTO;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.User;
import com.taskmanagement.enums.TaskStatus;
import com.taskmanagement.service.TaskService;
import com.taskmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserService userService;  // Add UserService to fetch actual users
    
    // Add new task
    @PostMapping
    public ResponseEntity<ApiResponse> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        try {
            Task task = new Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setStatus(taskDTO.getStatus() != null ? taskDTO.getStatus() : TaskStatus.PENDING);
            
            // Fetch the actual user from database if assignedUserId is provided
            if (taskDTO.getAssignedUserId() != null) {
                User assignedUser = userService.getUserById(taskDTO.getAssignedUserId());
                task.setAssignedUser(assignedUser);
            }
            
            Task createdTask = taskService.createTask(task);
            TaskDTO responseDTO = convertToDTO(createdTask);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Task created successfully", responseDTO));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, "Error creating task: " + e.getMessage()));
        }
    }
    
    // Display all tasks
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        List<TaskDTO> taskDTOs = tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(taskDTOs);
    }
    
    // Get task by ID (display task details)
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        try {
            Task task = taskService.getTaskById(id);
            return ResponseEntity.ok(convertToDTO(task));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, "Task not found with id: " + id));
        }
    }
    
    // Filter tasks by status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTasksByStatus(@PathVariable String status) {
        try {
            TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase().replace(" ", "_"));
            List<Task> tasks = taskService.getTasksByStatus(taskStatus);
            List<TaskDTO> taskDTOs = tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(taskDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, "Invalid status. Use: PENDING, IN_PROGRESS, or COMPLETED"));
        }
    }
    
    // Update task
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskDTO taskDTO) {
        
        try {
            Task taskDetails = new Task();
            taskDetails.setTitle(taskDTO.getTitle());
            taskDetails.setDescription(taskDTO.getDescription());
            taskDetails.setStatus(taskDTO.getStatus());
            
            // Fetch the actual user from database if assignedUserId is provided
            if (taskDTO.getAssignedUserId() != null) {
                User assignedUser = userService.getUserById(taskDTO.getAssignedUserId());
                taskDetails.setAssignedUser(assignedUser);
            }
            
            Task updatedTask = taskService.updateTask(id, taskDetails);
            TaskDTO responseDTO = convertToDTO(updatedTask);
            
            return ResponseEntity.ok(
                new ApiResponse(true, "Task updated successfully", responseDTO));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, "Error updating task: " + e.getMessage()));
        }
    }
    
    // Assign task to user
    @PutMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<ApiResponse> assignTask(
            @PathVariable Long taskId,
            @PathVariable(required = false) Long userId) {
        
        try {
            Task task = taskService.assignTaskToUser(taskId, userId);
            TaskDTO responseDTO = convertToDTO(task);
            
            return ResponseEntity.ok(
                new ApiResponse(true, "Task assigned successfully", responseDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, "Error assigning task: " + e.getMessage()));
        }
    }
    
    // Unassign task from user
    @PutMapping("/{taskId}/unassign")
    public ResponseEntity<ApiResponse> unassignTask(@PathVariable Long taskId) {
        try {
            Task task = taskService.assignTaskToUser(taskId, null);
            TaskDTO responseDTO = convertToDTO(task);
            
            return ResponseEntity.ok(
                new ApiResponse(true, "Task unassigned successfully", responseDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, "Error unassigning task: " + e.getMessage()));
        }
    }
    
    // Remove task from list
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok(new ApiResponse(true, "Task deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, "Task not found with id: " + id));
        }
    }
    
    // Get tasks by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTasksByUser(@PathVariable Long userId) {
        try {
            List<Task> tasks = taskService.getTasksByUser(userId);
            List<TaskDTO> taskDTOs = tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(taskDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, "User not found with id: " + userId));
        }
    }
    
    // Helper method to convert Task to TaskDTO
    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        
        if (task.getAssignedUser() != null) {
            dto.setAssignedUserId(task.getAssignedUser().getId());
            dto.setAssignedUsername(task.getAssignedUser().getUsername());
            dto.setAssignedUserEmail(task.getAssignedUser().getEmail());
        }
        
        return dto;
    }
}