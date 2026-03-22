package com.project.habit_tracker.Service;

import com.project.habit_tracker.Model.AuditLogAction;
import com.project.habit_tracker.Model.AuditLogEntityType;
import com.project.habit_tracker.Model.AuditLogResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AuditLogServiceTest {

    @Autowired
    private AuditLogService auditLogService;

    @Test
    void testAuditLogEvent() {
        System.out.println("\n========== Testing Audit Log Event ==========");
        
        auditLogService.logEvent(
                "testuser",
                AuditLogAction.CREATE,
                AuditLogEntityType.USER,
                "testuser",
                "127.0.0.1",
                "Test user registered",
                AuditLogResult.SUCCESS,
                null,
                null
        );
        
        System.out.println("========== Test Completed - Check database and console logs ==========\n");
    }
}

