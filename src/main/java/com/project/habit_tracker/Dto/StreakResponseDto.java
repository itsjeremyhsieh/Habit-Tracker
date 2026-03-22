package com.project.habit_tracker.Dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StreakResponseDto {
    private int id;
    private int habitId;
    private LocalDate completeDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
