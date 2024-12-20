package com.shopery.vendor.inventory.services;

import com.shopery.vendor.inventory.dto.AppUsersDto;
import com.shopery.vendor.inventory.dto.LoginDto;

import java.util.List;
import java.util.UUID;

public interface AppUsersService {

    AppUsersDto addUsers(AppUsersDto appUsersDto);

    LoginDto login(LoginDto loginDto);

    AppUsersDto updateUser(UUID userId, AppUsersDto appUsersDto);

    void softDeleteUser(UUID userId);

    AppUsersDto getUserById(UUID userId);

    List<AppUsersDto> getAllUsers();

    void saveTwoFactorCode(String email, String twoFactorCode);

    boolean verifyTwoFactorCode(String email, String code);

    AppUsersDto getUserByEmail(String email);
    String generateResetToken(String email);
    void sendResetPasswordEmail(String email, String token);

}
