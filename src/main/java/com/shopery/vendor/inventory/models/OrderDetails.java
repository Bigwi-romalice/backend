package com.shopery.vendor.inventory.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order; // Parent order

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Products product; // Associated product

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @Transient
    private BigDecimal subtotal; // Calculated dynamically

    @Column(nullable = false)
    private boolean isDeleted = false; // Soft delete

    // Lifecycle hook to calculate subtotal after loading the entity
    @PostLoad
    private void calculateSubtotal() {
        if (price != null && quantity > 0) {
            this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.subtotal = BigDecimal.ZERO; // Default if price or quantity is invalid
        }
    }
}
