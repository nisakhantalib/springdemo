package com.taskmanagement.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.taskmanagement.entity.User;
import com.taskmanagement.entity.Task;
import com.taskmanagement.enums.TaskStatus;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.repository.TaskRepository;

import java.util.Optional;

@Configuration
public class DataConfig {
    
    // Initialize sample data for development
    @Bean
    CommandLineRunner init(UserRepository userRepository, TaskRepository taskRepository) {
        return args -> {
            // --- Create sample users ---
            Optional<User> existingJohn = userRepository.findByEmail("john@example.com");
            User user1;
            if (existingJohn.isEmpty()) {
                user1 = new User();
                user1.setUsername("john_doe");
                user1.setEmail("john@example.com");
                user1.setPassword("password123"); // In production, encrypt this
                user1 = userRepository.save(user1);
                System.out.println("Inserted user: john@example.com");
            } else {
                user1 = existingJohn.get();
                System.out.println("User already exists: john@example.com");
            }

            Optional<User> existingJane = userRepository.findByEmail("jane@example.com");
            User user2;
            if (existingJane.isEmpty()) {
                user2 = new User();
                user2.setUsername("jane_smith");
                user2.setEmail("jane@example.com");
                user2.setPassword("password123");
                user2 = userRepository.save(user2);
                System.out.println("Inserted user: jane@example.com");
            } else {
                user2 = existingJane.get();
                System.out.println("User already exists: jane@example.com");
            }

            // --- Create sample tasks (only if not already in DB) ---
            if (taskRepository.count() == 0) {
                Task task1 = new Task();
                task1.setTitle("Complete Project Documentation");
                task1.setDescription("Write comprehensive documentation for the project");
                task1.setStatus(TaskStatus.PENDING);
                task1.setAssignedUser(user1);
                taskRepository.save(task1);

                Task task2 = new Task();
                task2.setTitle("Review Code");
                task2.setDescription("Review and test the new feature implementation");
                task2.setStatus(TaskStatus.IN_PROGRESS);
                task2.setAssignedUser(user2);
                taskRepository.save(task2);

                Task task3 = new Task();
                task3.setTitle("Deploy to Production");
                task3.setDescription("Deploy the latest version to production server");
                task3.setStatus(TaskStatus.COMPLETED);
                task3.setAssignedUser(user1);
                taskRepository.save(task3);

                System.out.println("Sample tasks inserted.");
            } else {
                System.out.println("Tasks already exist, skipping insert.");
            }
        };
    }
}
