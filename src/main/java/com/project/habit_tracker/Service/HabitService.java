package com.project.habit_tracker.Service;

import com.project.habit_tracker.Model.Habit;
import com.project.habit_tracker.Repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {
    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public List<Habit> getAllHabits() {
        return habitRepository.findAll();
    }

    public List<Habit> getHabitsByUserId(int userId) {
        return habitRepository.findByUserId(userId);
    }

    public List<Habit> getHabitsByCompletedToday(Boolean completedToday) {
        return habitRepository.findByCompletedToday(completedToday);
    }

    public List<Habit> getHabitsByUserIdAndCompletedToday(int userId, Boolean completedToday) {
        return habitRepository.findByUserIdAndCompletedToday(userId, completedToday);
    }

    public List<Habit> getHabitsByName(String name) {
        return habitRepository.findByName(name);
    }

    public Habit getHabitById(int id) {
        return habitRepository.findById(id);
    }

    public Habit createHabit(Habit habit) {
        return habitRepository.save(habit);
    }
    // todo: Update, delete
}
