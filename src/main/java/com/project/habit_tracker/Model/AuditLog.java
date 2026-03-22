package com.project.habit_tracker.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name = "audit_log")
@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    @Enumerated(EnumType.ORDINAL)
    private AuditLogAction action;
    @Column(name = "entity_type")
    @Enumerated(EnumType.ORDINAL)
    private AuditLogEntityType entityType;
    @Column(name = "entity_id")
    private String entityId;
    @Column(name = "ip_address")
    private String ipAddress;
    private String message;
    @Column(name= "old_data")
    private String oldData;
    @Column(name= "new_data")
    private String newData;
    @Enumerated(EnumType.ORDINAL)
    private AuditLogResult result;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_At")
    private LocalDateTime updatedAt;

    public AuditLog() {
    }

    public AuditLog(String username, AuditLogAction action, AuditLogEntityType entityType, String entityId, String ipAddress, String message, AuditLogResult result, String oldData, String newData) {
        this.username = username;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.ipAddress = ipAddress;
        this.message = message;
        this.result = result;
        this.oldData = oldData;
        this.newData = newData;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
