package com.taskmanagement.dto;

import com.taskmanagement.enums.TaskStatus;

public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long assignedUserId;
    private String assignedUsername;
    private String assignedUserEmail;
    
    // Default constructor
    public TaskDTO() {
    }
    
    // Constructor with all fields
    public TaskDTO(Long id, String title, String description, TaskStatus status, 
                   Long assignedUserId, String assignedUsername, String assignedUserEmail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignedUserId = assignedUserId;
        this.assignedUsername = assignedUsername;
        this.assignedUserEmail = assignedUserEmail;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public TaskStatus getStatus() {
        return status;
    }
    
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    
    public Long getAssignedUserId() {
        return assignedUserId;
    }
    
    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }
    
    public String getAssignedUsername() {
        return assignedUsername;
    }
    
    public void setAssignedUsername(String assignedUsername) {
        this.assignedUsername = assignedUsername;
    }
    
    public String getAssignedUserEmail() {
        return assignedUserEmail;
    }
    
    public void setAssignedUserEmail(String assignedUserEmail) {
        this.assignedUserEmail = assignedUserEmail;
    }
    
    @Override
    public String toString() {
        return "TaskDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", assignedUserId=" + assignedUserId +
                ", assignedUsername='" + assignedUsername + '\'' +
                ", assignedUserEmail='" + assignedUserEmail + '\'' +
                '}';
    }
}