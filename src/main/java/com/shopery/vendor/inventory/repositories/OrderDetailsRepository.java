package com.shopery.vendor.inventory.repositories;

import com.shopery.vendor.inventory.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, UUID> {
}
