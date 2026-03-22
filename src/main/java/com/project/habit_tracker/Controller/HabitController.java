package com.project.habit_tracker.Controller;

import com.project.habit_tracker.Dto.HabitResponseDto;
import com.project.habit_tracker.Model.Habit;
import com.project.habit_tracker.Service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/habits")
public class HabitController {
    @Autowired
    private HabitService habitService;

    @GetMapping
    public ResponseEntity<List<HabitResponseDto>> getAllHabits(){
        List<Habit> habits = habitService.getAllHabits();
        List<HabitResponseDto> habitDtos = new ArrayList<>();

        for (var habit : habits){
            var habitDto = habitToDto(habit);
            habitDtos.add(habitDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(habitDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitResponseDto> getHabitById(int id){
        Habit habit = habitService.getHabitById(id);
        if (habit == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        HabitResponseDto habitDto = habitToDto(habit);
        return ResponseEntity.status(HttpStatus.OK).body(habitDto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HabitResponseDto>> getHabitsByUserId(int userId){
        List<Habit> habits = habitService.getHabitsByUserId(userId);
        List<HabitResponseDto> habitDtos = new ArrayList<>();

        for (var habit : habits){
            var habitDto = habitToDto(habit);
            habitDtos.add(habitDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(habitDtos);
    }

//    public ResponseEntity<List<CompleteStreakDTO>> getHabitCompleteStreakByHabitId(int habitId) {
//        List<CompleteStreakDTO> completeStreaks = habitService.getHabitCompleteStreakByHabitId(habitId);
//        return ResponseEntity.status(HttpStatus.OK).body(completeStreaks);
//    }

    private HabitResponseDto habitToDto(Habit habit) {
        HabitResponseDto habitDto = new HabitResponseDto();
        habitDto.setId(habit.getId());
        habitDto.setName(habit.getName());
        habitDto.setDescription(habit.getDescription());
        habitDto.setUserId(habit.getUserId());
        habitDto.setCompletedToday(habit.isCompletedToday());
        habitDto.setCreatedAt(habit.getCreatedAt());
        habitDto.setUpdatedAt(habit.getUpdatedAt());
        return habitDto;
    }
}
