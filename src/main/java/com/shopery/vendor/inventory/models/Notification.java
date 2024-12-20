package com.shopery.vendor.inventory.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUsers user; // Notification is for a specific user

    @Column(nullable = false)
    private String message; // Content of the notification

    @Enumerated(EnumType.STRING)
    private NotificationType type; // INFO, WARNING, ERROR, etc.

    @Enumerated(EnumType.STRING)
    private NotificationStatus status; // READ, UNREAD

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date(); // When the notification was created

    @Temporal(TemporalType.TIMESTAMP)
    private Date readAt; // When the notification was read (nullable)

    @Column(nullable = false)
    private boolean isDeleted = false; // Soft delete flag

    // Lifecycle hook to update `readAt` when status is set to READ
    @PreUpdate
    private void onUpdate() {
        if (this.status == NotificationStatus.READ && this.readAt == null) {
            this.readAt = new Date();
        }
    }
}
