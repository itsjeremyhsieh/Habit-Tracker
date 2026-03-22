package com.project.habit_tracker.Repository;

import com.project.habit_tracker.Model.Streak;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StreakRepository extends JpaRepository<Streak, Long> {
    List<Streak> findAll();
    Streak findById(int id);
    List<Streak> findByHabitId(int habitId);
    Streak findByHabitIdAndCompleteDate(int habitId, LocalDate date);

}
