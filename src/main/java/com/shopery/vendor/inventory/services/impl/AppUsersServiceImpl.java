package com.shopery.vendor.inventory.services.impl;

import com.shopery.vendor.inventory.dto.AppUsersDto;
import com.shopery.vendor.inventory.dto.LoginDto;
import com.shopery.vendor.inventory.models.AppUsers;
import com.shopery.vendor.inventory.models.UserType;
import com.shopery.vendor.inventory.repositories.AppUsersRepository;
import com.shopery.vendor.inventory.services.AppUsersService;
import com.shopery.vendor.inventory.services.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppUsersServiceImpl implements AppUsersService {

    // In-memory map to store 2FA codes temporarily
    private Map<String, TwoFactorCodeData> twoFactorCodes = new HashMap<>();
    private static final long EXPIRATION_TIME = 5 * 60 * 1000; // 5 minutes

    @Autowired
    private AppUsersRepository appUsersRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private final ModelMapper modelMapper;

    public  AppUsersServiceImpl(ModelMapper modelMapper ){
        this.modelMapper = modelMapper;
    }

    @Override
    public AppUsersDto addUsers(AppUsersDto appUsersDto) {
        if (appUsersRepository.existsByEmail(appUsersDto.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        // Convert AppUsersDto to AppUsers JPA entity
        AppUsers appUsers = new AppUsers();
        appUsers.setUserType(UserType.valueOf(appUsersDto.getUserType()));
        appUsers.setName(appUsersDto.getName());
        appUsers.setEmail(appUsersDto.getEmail());
        appUsers.setPhoneNumber(appUsersDto.getPhoneNumber());
        appUsers.setAddress(appUsersDto.getAddress());
        appUsers.setStatus(appUsersDto.getStatus());
        appUsers.setLatitude(appUsersDto.getLatitude());
        appUsers.setLongitude(appUsersDto.getLongitude());
        appUsers.setIsTwoFactorEnabled(appUsersDto.getIsTwoFactorEnabled());
        appUsers.setIsTwoFactorVerified(appUsersDto.getIsTwoFactorVerified());
        appUsers.setPassword(appUsersDto.getPassword());

        // Save the entity to the repository
        AppUsers savedUser = appUsersRepository.save(appUsers);

        return convertToDto(savedUser);
    }

    @Override
    public LoginDto login(LoginDto loginDto) {
        AppUsers user = appUsersRepository.findByEmail(loginDto.getEmail());
        if (user == null || !loginDto.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return modelMapper.map(user, LoginDto.class);
    }

    @Override
    public AppUsersDto updateUser(UUID userId, AppUsersDto appUsersDto) {
        AppUsers existingUser = appUsersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Update fields as necessary
        existingUser.setName(appUsersDto.getName());
        existingUser.setPhoneNumber(appUsersDto.getPhoneNumber());
        existingUser.setAddress(appUsersDto.getAddress());
        existingUser.setStatus(appUsersDto.getStatus());
        existingUser.setLatitude(appUsersDto.getLatitude());
        existingUser.setLongitude(appUsersDto.getLongitude());
        existingUser.setIsTwoFactorEnabled(appUsersDto.getIsTwoFactorEnabled());
        existingUser.setIsTwoFactorVerified(appUsersDto.getIsTwoFactorVerified());

        // Save the updated user
        AppUsers updatedUser = appUsersRepository.save(existingUser);

        return convertToDto(updatedUser);
    }

    @Override
    public void softDeleteUser(UUID userId) {
        AppUsers user = appUsersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setStatus(true); // Mark as deleted
        appUsersRepository.save(user);
    }

    @Override
    public AppUsersDto getUserById(UUID userId) {
        AppUsers user = appUsersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return convertToDto(user);
    }

    @Override
    public List<AppUsersDto> getAllUsers() {
        List<AppUsers> users = appUsersRepository.findAll();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Save 2FA code temporarily
    @Override
    public void saveTwoFactorCode(String email, String twoFactorCode) {
        long currentTime = System.currentTimeMillis();
        TwoFactorCodeData codeData = new TwoFactorCodeData(twoFactorCode, currentTime);
        twoFactorCodes.put(email, codeData);
    }

    // Verify 2FA code
    @Override
    public boolean verifyTwoFactorCode(String email, String code) {
        TwoFactorCodeData codeData = twoFactorCodes.get(email);
        if (codeData == null) {
            return false; // No code found for this email
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - codeData.getTimestamp() > EXPIRATION_TIME) {
            // Code has expired, remove it and return false
            twoFactorCodes.remove(email);
            return false;
        }

        // Code matches and is not expired
        return code.equals(codeData.getCode());
    }

    // Helper method to convert AppUsers entity to AppUsersDto
    private AppUsersDto convertToDto(AppUsers appUsers) {
        return new AppUsersDto(
                appUsers.getUserType().name(),
                appUsers.getName(),
                appUsers.getEmail(),
                appUsers.getPhoneNumber(),
                appUsers.getAddress(),
                appUsers.getStatus(),
                appUsers.getLatitude(),
                appUsers.getLongitude(),
                appUsers.getIsTwoFactorEnabled(),
                appUsers.getIsTwoFactorVerified(),
                appUsers.getPassword(),
                appUsers.getResetToken(),
                appUsers.getTokenExpiryDate()
                );
    }

    // Helper class to store 2FA code and timestamp
    private static class TwoFactorCodeData {
        private String code;
        private long timestamp;

        public TwoFactorCodeData(String code, long timestamp) {
            this.code = code;
            this.timestamp = timestamp;
        }

        public String getCode() {
            return code;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    @Override
    public AppUsersDto getUserByEmail(String email) {
        // Fetch the user from the database using the repository
        AppUsers user = appUsersRepository.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("User with email " + email + " not found.");
        }

        // Convert the AppUsers entity to AppUsersDto
        return AppUsersDto.builder()
                .userType(UserType.CUSTOMER.name()) // Assuming userType is a String or Enum
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .status(user.getStatus())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .isTwoFactorEnabled(user.getIsTwoFactorEnabled())
                .isTwoFactorVerified(user.getIsTwoFactorVerified())
                .password(user.getPassword()) // You may want to exclude the password in a real case
                .build();
    }

    @Override
    public String generateResetToken(String email) {
        AppUsers user = appUsersRepository.findByEmail(email);
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusMinutes(30)); // Token valid for 30 minutes
        appUsersRepository.save(user);

        return token;
    }

    @Override
    public void sendResetPasswordEmail(String email, String token) {
        String resetLink = "http://localhost:5173/auth/reset-password/token=" + token;
        String subject = "Reset Your Password";
        String body = "Click the following link to reset your password: " + resetLink;

        emailService.sendEmail(email, subject, body);
    }

}
