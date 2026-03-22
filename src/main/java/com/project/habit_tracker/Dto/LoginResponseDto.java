package com.project.habit_tracker.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String name;
    private String username;
    private String role;
}

