package com.shopery.vendor.inventory.repositories;

import com.shopery.vendor.inventory.models.AppUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface AppUsersRepository extends JpaRepository<AppUsers, UUID> {
    boolean existsByEmail(String email);
    AppUsers findByEmail(String email);
    @Query("SELECT a FROM AppUsers a LEFT JOIN FETCH a.shops WHERE a.userId = :userId")
    AppUsers findByIdWithShops(@Param("userId") UUID userId);


    Optional<Object> findByResetToken(String token);
}
