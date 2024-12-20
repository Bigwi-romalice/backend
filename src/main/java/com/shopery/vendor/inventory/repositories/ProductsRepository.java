package com.shopery.vendor.inventory.repositories;

import com.shopery.vendor.inventory.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductsRepository extends JpaRepository<Products, UUID> {
    @Query("SELECT p FROM Products p WHERE p.shop.shopId = :shopId")
    List<Products> getProductsByShopId(@Param("shopId") UUID shopId);
}
