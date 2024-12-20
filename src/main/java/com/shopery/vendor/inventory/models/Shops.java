package com.shopery.vendor.inventory.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shops {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID shopId;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private AppUsers vendor; // A vendor who owns the shop

    @Column(nullable = false)
    private String shopName;

    private String shopAddress;

    @Column(nullable = true)
    private String shopLogo; // URL or path to the shop logo

    @Column(nullable = false)
    private String shopSpecialisation; // Shop specialization category

    @Column(nullable = true)
    private Double latitude; // Shop's geo-location latitude

    @Column(nullable = true)
    private Double longitude; // Shop's geo-location longitude

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // Lifecycle hook to update `updatedAt` on each modification
    @PreUpdate
    private void onUpdate() {
        updatedAt = new Date();
    }

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Products> products = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders = new ArrayList<>(); // Track orders associated with this shop

}
