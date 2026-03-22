package com.project.habit_tracker.Dto;

import java.time.LocalDateTime;

public class HabitRequestDto {
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
