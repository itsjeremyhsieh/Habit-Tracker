package com.project.habit_tracker.Repository;

import com.project.habit_tracker.Model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findAll();
    List<Habit> findByUserId(int userId);
    List<Habit> findByCompletedToday(Boolean completedToday);
    List<Habit> findByUserIdAndCompletedToday(int userId, Boolean completedToday);
    Habit findById(int id);
    List<Habit> findByName(String name);

}
