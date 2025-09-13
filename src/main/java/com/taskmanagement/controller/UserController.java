package com.taskmanagement.controller;

import com.taskmanagement.dto.ApiResponse;
import com.taskmanagement.dto.UserDTO;
import com.taskmanagement.entity.User;
import com.taskmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // Add new user, RequestBody indicates its JSON
    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserDTO userDTO) {
        User createdUser = userService.createUser(userDTO);
        
        UserDTO responseDTO = new UserDTO(
            createdUser.getId(),
            createdUser.getUsername(),
            createdUser.getEmail()
        );
        System.out.println(userDTO);
        System.out.println(responseDTO);
        return ResponseEntity
        		.status(HttpStatus.CREATED)
        		.body(new ApiResponse(true, "User created successfully", responseDTO));
    }
    
    // Display list of all users
    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
            .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()))
            .collect(Collectors.toList());
        
        return ResponseEntity
        		.status(HttpStatus.OK)
        		.body(new ApiResponse(true, "Users fetched succesfully", userDTOs));
    }
    
    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity
        		.status(HttpStatus.OK)
        		.body(new ApiResponse(true, "User fetched successfully",userDTO));
    }
    
    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {
        
        User userDetails = new User();
        userDetails.setUsername(userDTO.getUsername());
        userDetails.setEmail(userDTO.getEmail());
        userDetails.setPassword(userDTO.getPassword());
        
        User updatedUser = userService.updateUser(id, userDetails);
        
        UserDTO responseDTO = new UserDTO(
            updatedUser.getId(),
            updatedUser.getUsername(),
            updatedUser.getEmail()
        );
        
        return ResponseEntity.ok(
            new ApiResponse(true, "User updated successfully", responseDTO));
    }
    
    // Remove user from list
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully"));
    }
}

