package com.shopery.vendor.inventory.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productId;

    @ManyToOne // A product belongs to one shop
    @JoinColumn(name = "shop_id", nullable = false, referencedColumnName = "shopId")
    private Shops shop; // Product belongs to a shop

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = true)
    private String productPicture; // URL or path to the product picture

    @Column(nullable = true, length = 1000) // Allows for a detailed description of up to 1000 characters
    private String productDescription;

    @Enumerated(EnumType.STRING) // Store enum as a string in the database
    @Column(nullable = false)
    private ProductCategory productCategory; // Enum for product category

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(nullable = false)
    private boolean isDeleted = false; // Soft delete flag

    // Lifecycle hook to update `updatedAt` on each modification
    @PreUpdate
    private void onUpdate() {
        updatedAt = new Date();
    }
}
