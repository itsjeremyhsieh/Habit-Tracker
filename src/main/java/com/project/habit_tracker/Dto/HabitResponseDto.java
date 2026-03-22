package com.project.habit_tracker.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HabitResponseDto {
    private int id;
    private String name;
    private int userId;
    private String description;
    private int maxStreak;
    private int currentStreak;
    private boolean completedToday;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
