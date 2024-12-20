package com.shopery.vendor.inventory.repositories;

import com.shopery.vendor.inventory.dto.OrdersDto;
import com.shopery.vendor.inventory.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, UUID> {
    @Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId AND o.shop.shopId = :shopId AND o.isDeleted = false")
    List<Orders> findOrdersByCustomerAndShop(UUID customerId, UUID shopId);

    @Query("SELECT o FROM Orders o WHERE  o.shop.shopId = :shopId AND o.isDeleted = false")
    List<Orders> findOrderByShop(UUID shopId);


}
