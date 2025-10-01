package com.taskmanagement.controller;

import com.taskmanagement.dto.UserDTO;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.User;
import com.taskmanagement.enums.TaskStatus;
import com.taskmanagement.service.TaskService;
import com.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TaskService taskService;
    
    // Login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    // Home page - redirect to users
    @GetMapping({"/", "/home"})
    public String home() {
        return "redirect:/users";
    }
    
    // User Management Page
    @GetMapping("/users")
    public String users(Model model) {
    	
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("activeTab", "users"); // Add this for nav highlighting
        return "users";
    }
    
    // Add User
    @PostMapping("/users/add")
    public String addUser(@ModelAttribute("newUser") UserDTO userDTO, RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(userDTO);
            redirectAttributes.addFlashAttribute("successMessage", "User added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/users";
    }
    
    // Delete User
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/users";
    }
    
    // Task Management Page
    @GetMapping("/tasks")
    public String tasks(@RequestParam(required = false) String status, 
                       Model model) {
        try {
            if (status != null && !status.isEmpty()) {
                model.addAttribute("tasks", taskService.getTasksByStatus(TaskStatus.valueOf(status)));
                model.addAttribute("currentFilter", status);
            } else {
                model.addAttribute("tasks", taskService.getAllTasks());
                model.addAttribute("currentFilter", "ALL");
            }
            
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("newTask", new Task());
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("currentUser", userDetails != null ? userDetails.getUsername() : "Guest");
            model.addAttribute("activeTab", "tasks"); // Add this for nav highlighting
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading tasks: " + e.getMessage());
            model.addAttribute("tasks", java.util.Collections.emptyList());
            model.addAttribute("users", java.util.Collections.emptyList());
            model.addAttribute("newTask", new Task());
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("currentFilter", "ALL");
            model.addAttribute("activeTab", "tasks");
        }
        return "tasks";
    }
    
    // Add Task
    @PostMapping("/tasks/add")
    public String addTask(@ModelAttribute Task task, 
                         @RequestParam(required = false) Long assignedUserId,
                         RedirectAttributes redirectAttributes) {
        try {
            if (assignedUserId != null) {
                User user = userService.getUserById(assignedUserId);
                task.setAssignedUser(user);
            }
            taskService.createTask(task);
            redirectAttributes.addFlashAttribute("successMessage", "Task added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/tasks";
    }
    
    // Delete Task
    @GetMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            taskService.deleteTask(id);
            redirectAttributes.addFlashAttribute("successMessage", "Task deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/tasks";
    }
    
    // Update Task Status
    @GetMapping("/tasks/status/{id}/{status}")
    public String updateTaskStatus(@PathVariable Long id, 
                                  @PathVariable String status,
                                  RedirectAttributes redirectAttributes) {
        try {
            Task task = taskService.getTaskById(id);
            task.setStatus(TaskStatus.valueOf(status));
            taskService.updateTask(id, task);
            redirectAttributes.addFlashAttribute("successMessage", "Task status updated!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/tasks";
    }
}
