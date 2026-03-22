package com.project.habit_tracker.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private int id;
    private String username;
    private String name;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
