package com.project.habit_tracker.Service;

import com.project.habit_tracker.Model.AuditLog;
import com.project.habit_tracker.Repository.AuditLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class AuditLogEventListener {

    private final AuditLogRepository auditLogRepository;

    public AuditLogEventListener(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleAuditLogEvent(AuditLog auditLog) {
        try {
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("\n✗✗✗ LISTENER ERROR - Failed to save audit log", e);
        }
    }
}

