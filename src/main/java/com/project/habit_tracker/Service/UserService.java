package com.project.habit_tracker.Service;

import com.project.habit_tracker.Model.AuditLogAction;
import com.project.habit_tracker.Model.AuditLogEntityType;
import com.project.habit_tracker.Model.AuditLogResult;
import com.project.habit_tracker.Model.User;
import com.project.habit_tracker.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService = auditLogService;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(int id){
        return userRepository.findById(id);
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> getOptionalUserByUsername(String username) {
        return userRepository.findOptionalByUsername(username);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User registerUser(String username, String name, String email, String rawPassword) {
        
        String normalizedUsername = username.trim().toLowerCase();
        String normalizedName = name.trim();
        String normalizedEmail = email.trim().toLowerCase();

        if (userRepository.findOptionalByUsername(normalizedUsername).isPresent()) {
            log.warn("Username already exists: {}", normalizedUsername);
            throw new IllegalArgumentException("Username already exists");
        }

        log.info("Checking if email exists...");
        if (userRepository.findOptionalByEmail(normalizedEmail).isPresent()) {
            log.warn("Email already exists: {}", normalizedEmail);
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(normalizedUsername);
        user.setName(normalizedName);
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(rawPassword));

        User savedUser = userRepository.save(user);
        try {
            auditLogService.logEvent(
                normalizedUsername, 
                AuditLogAction.CREATE, 
                AuditLogEntityType.USER, 
                normalizedUsername, 
                "127.0.0.1", 
                "User registered", 
                AuditLogResult.SUCCESS, 
                null, 
                null
            );
        } catch (Exception e) {
            log.error("✗✗✗ Error calling auditLogService.logEvent()", e);
            e.printStackTrace();
        }
        return savedUser;
    }
}
