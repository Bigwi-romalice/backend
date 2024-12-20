package com.shopery.vendor.inventory.controllers;

import com.shopery.vendor.inventory.dto.AppUsersDto;
import com.shopery.vendor.inventory.dto.LoginDto;
import com.shopery.vendor.inventory.models.AppUsers;
import com.shopery.vendor.inventory.repositories.AppUsersRepository;
import com.shopery.vendor.inventory.services.AppUsersService;
import com.shopery.vendor.inventory.services.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/users")
public class AppUsersController {

    @Autowired
    private AppUsersService appUsersService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AppUsersRepository appUsersRepository;


    // Create User
    @Operation(summary = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody AppUsersDto appUsersDto) {
        try {
            System.out.println("Received: " + appUsersDto);
            AppUsersDto savedUsers = appUsersService.addUsers(appUsersDto);
            return new ResponseEntity<>(savedUsers, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Logs in the user using email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Invalid email or password.")
    })
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            // 1. Authenticate the user
            LoginDto loggedInUser = appUsersService.login(loginDto);

            // 2. Generate a 2FA code
            String twoFactorCode = generateTwoFactorCode();

            // 3. Send the 2FA code to the user's email
            emailService.sendTwoFactorCode(loginDto.getEmail(), twoFactorCode);

            // Store the generated 2FA code temporarily for verification later
            // (This can be stored in the session, database, or cache for a limited time)
            // Example: Saving the code in a temporary store (e.g., cache, session, etc.)
            appUsersService.saveTwoFactorCode(loginDto.getEmail(), twoFactorCode);

            return new ResponseEntity<>("2FA code sent to email", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    // Helper method to generate a random 6-digit 2FA code
    private String generateTwoFactorCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6-digit random number
        return String.valueOf(code);
    }

    @PostMapping("/verify-2fa")
    @Operation(summary = "Verify 2FA code", description = "Verifies the 2FA code entered by the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "2FA code verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid 2FA code")
    })
    public ResponseEntity<?> verifyTwoFactorCode(@RequestParam String email, @RequestParam String code) {
        try {
            boolean isVerified = appUsersService.verifyTwoFactorCode(email, code);
            if (isVerified) {
                // Fetch user details by email
                AppUsersDto userDto = appUsersService.getUserByEmail(email); // Assuming you have a method to get user
                                                                             // by email

                // Return response with user name and role
                return new ResponseEntity<>(Map.of(
                        "message", "2FA code verified successfully",
                        "name", userDto.getName(),
                        "role", userDto.getUserType()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid 2FA code", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update user details", description = "Updates the information of an existing user by user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or user not found.")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody AppUsersDto appUsersDto) {
        try {
            AppUsersDto updatedUser = appUsersService.updateUser(userId, appUsersDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Soft delete user", description = "Soft deletes a user by setting the 'isDeleted' flag to true.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User soft deleted successfully"),
            @ApiResponse(responseCode = "400", description = "User not found or invalid ID.")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> softDeleteUser(@PathVariable UUID userId) {
        try {
            appUsersService.softDeleteUser(userId);
            return new ResponseEntity<>("User soft deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Get User by ID
    @Operation(summary = "Retrieve user details", description = "Fetches the details of a user based on their user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId) {
        try {
            AppUsersDto user = appUsersService.getUserById(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Get All Users
    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<List<AppUsersDto>> getAllUsers() {
        List<AppUsersDto> users = appUsersService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @Operation(summary = "Reset Password")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email) {
        try {
            String token = appUsersService.generateResetToken(email);
            appUsersService.sendResetPasswordEmail(email, token);

            return ResponseEntity.ok("Reset password email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @Operation(summary = "Verify the Reset Link")
    @PostMapping("/reset-password/verify")
    public ResponseEntity<?> verifyAndResetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            AppUsers user = (AppUsers) appUsersRepository.findByResetToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

            if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has expired");
            }

            user.setPassword(newPassword); // Hash the password before saving
            user.setResetToken(null); // Invalidate the token
            user.setTokenExpiryDate(null);
            appUsersRepository.save(user);

            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
