package com.project.habit_tracker.Service;

import com.project.habit_tracker.Model.AuditLog;
import com.project.habit_tracker.Model.AuditLogAction;
import com.project.habit_tracker.Model.AuditLogEntityType;
import com.project.habit_tracker.Model.AuditLogResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditLogService {

    private final ApplicationEventPublisher eventPublisher;

    public AuditLogService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void logEvent(String username, AuditLogAction action, AuditLogEntityType entityType, String entityId, String ipAddress, String message, AuditLogResult result, String old_data, String new_data) {
        
        try {
            AuditLog auditLog = new AuditLog(username, action, entityType, entityId, ipAddress, message, result, old_data, new_data);

            eventPublisher.publishEvent(auditLog);
            
        } catch (Exception e) {
            log.error("✗✗✗ ERROR in logEvent()", e);
            e.printStackTrace();
        }
    }
}
