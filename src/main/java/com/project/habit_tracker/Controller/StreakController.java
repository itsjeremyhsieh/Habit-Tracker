package com.project.habit_tracker.Controller;

import com.project.habit_tracker.Dto.StreakResponseDto;
import com.project.habit_tracker.Model.Streak;
import com.project.habit_tracker.Service.StreakService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/streaks")
public class StreakController {
    private final StreakService streakService;

    public StreakController(StreakService streakService) {
        this.streakService = streakService;
    }
    @GetMapping
    public ResponseEntity<List<StreakResponseDto>> getAllStreaks() {
        List<Streak> streaks = streakService.getAllStreaks();
        return ResponseEntity.status(HttpStatus.OK).body(streaks.stream().map(this::streakToDto).toList());
    }

    @GetMapping("/habit/{habitId}")
    public ResponseEntity<List<StreakResponseDto>> getStreaksByHabitId(int habitId) {
        List<Streak> streaks = streakService.getStreaksByHabitId(habitId);
        return ResponseEntity.status(HttpStatus.OK).body(streaks.stream().map(this::streakToDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StreakResponseDto> getStreakById(int id) {
        Streak streak = streakService.getStreakById(id);
        if (streak == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(streakToDto(streak));
    }
    
    public ResponseEntity<StreakResponseDto> getStreakByHabitIdAndDate(int habitId, LocalDate date) {
        Streak streak = streakService.getStreakByHabitIdAndDate(habitId, date);
        if (streak == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(streakToDto(streak));
    }

    protected StreakResponseDto streakToDto(Streak streak){
        StreakResponseDto streakDto = new StreakResponseDto();
        streakDto.setId(streak.getId());
        streakDto.setHabitId(streak.getHabitId());
        streakDto.setCompleteDate(streak.getCompleteDate());
        streakDto.setCreatedAt(streak.getCreatedAt());
        streakDto.setUpdatedAt(streak.getUpdatedAt());
        return streakDto;
    }
}
