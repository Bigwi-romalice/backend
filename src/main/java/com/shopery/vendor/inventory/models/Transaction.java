package com.shopery.vendor.inventory.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID transactionId;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private AppUsers vendor;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // PENDING, COMPLETED, FAILED

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(nullable = false)
    private boolean isDeleted = false; // Soft delete flag

    // Lifecycle hook to set default transaction date and createdAt
    @PrePersist
    private void onCreate() {
        if (transactionDate == null) {
            transactionDate = new Date(); // Set the current date if not provided
        }
        if (createdAt == null) {
            createdAt = new Date(); // Ensure createdAt is set
        }
    }

    // Lifecycle hook to update updatedAt on modifications
    @PreUpdate
    private void onUpdate() {
        updatedAt = new Date(); // Update the timestamp when modified
    }

}
