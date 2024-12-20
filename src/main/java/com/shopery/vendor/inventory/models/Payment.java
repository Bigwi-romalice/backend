package com.shopery.vendor.inventory.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID paymentId;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // CARD, CASH, BANK_TRANSFER, MOMO

    private BigDecimal amountPaid;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(nullable = false)
    private boolean isDeleted = false; // Soft delete flag

    // Lifecycle hook to set default values before persisting the entity
    @PrePersist
    private void onCreate() {
        if (paymentDate == null) {
            paymentDate = new Date(); // Default to current time if not provided
        }
        if (createdAt == null) {
            createdAt = new Date(); // Ensure createdAt is set
        }
    }

    // Lifecycle hook to update `updatedAt` on each modification
    @PreUpdate
    private void onUpdate() {
        updatedAt = new Date(); // Update the timestamp on each modification
    }
}
