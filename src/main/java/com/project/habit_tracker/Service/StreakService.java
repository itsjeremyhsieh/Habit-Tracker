package com.project.habit_tracker.Service;


import com.project.habit_tracker.Model.Streak;
import com.project.habit_tracker.Repository.StreakRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StreakService {
    private final StreakRepository streakRepository;

    public StreakService(StreakRepository streakRepository) {
        this.streakRepository = streakRepository;
    }

    public List<Streak> getAllStreaks() {
        return streakRepository.findAll();
    }

    public Streak getStreakById(int id) {
        return streakRepository.findById(id);
    }

    public List<Streak> getStreaksByHabitId(int habitId) {
        return streakRepository.findByHabitId(habitId);
    }

    public Streak getStreakByHabitIdAndDate(int habitId, LocalDate date) {
        return streakRepository.findByHabitIdAndCompleteDate(habitId, date);
    }

}
