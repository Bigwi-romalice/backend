package com.shopery.vendor.inventory.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUsersDto {

    private String userType; // CUSTOMER, VENDOR, ADMIN
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private Boolean status;
    private Double latitude;
    private Double longitude;

    // 2FA related fields
    private Boolean isTwoFactorEnabled; // To indicate if 2FA is enabled
    private Boolean isTwoFactorVerified; // To indicate if the user has verified 2FA

    // Password field
    private String password; // To store the user's password
    private String resetToken;
    private LocalDateTime tokenExpiryDate;

}
