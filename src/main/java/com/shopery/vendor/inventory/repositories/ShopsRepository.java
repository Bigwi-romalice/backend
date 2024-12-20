package com.shopery.vendor.inventory.repositories;

import com.shopery.vendor.inventory.models.Shops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShopsRepository extends JpaRepository<Shops, UUID> {
    List<Shops> findByVendorUserId(UUID userId);
}
