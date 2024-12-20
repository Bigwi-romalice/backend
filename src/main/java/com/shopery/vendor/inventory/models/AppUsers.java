package com.shopery.vendor.inventory.models;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class AppUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private UserType userType; // CUSTOMER, VENDOR, ADMIN

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    private String address;

    private String password;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Shops> shops = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Orders> orders = new ArrayList<>();

    @Column(name = "is_deleted", nullable = true)
    private Boolean status = false;

    // Optional geo-location fields
    @Column(nullable = true)
    private Double latitude; // Geo-location latitude

    @Column(nullable = true)
    private Double longitude; // Geo-location longitude

    // 2FA related fields
    private Boolean isTwoFactorEnabled = false; // Flag to indicate if 2FA is enabled
    private String googleAuthenticatorSecret; // Google Authenticator secret key (if using TOTP)
    private Boolean isTwoFactorVerified = false; // To indicate if the user has verified 2FA

    @Column(length = 6) // A 6-character code for 2FA (e.g., OTP)
    private String twoFactorCode; // 2FA code for verification
    private String resetToken;
    private LocalDateTime tokenExpiryDate;

}
