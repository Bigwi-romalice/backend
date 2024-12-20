package com.shopery.vendor.inventory.services;

import com.shopery.vendor.inventory.dto.ShopDto;
import com.shopery.vendor.inventory.models.Shops;

import java.util.List;
import java.util.UUID;

public interface ShopsService {
    ShopDto createShop(ShopDto shopDto);
    Shops updateShop(UUID shopId, ShopDto shopDto);
    void deleteShop(UUID shopId);
    ShopDto getShopById(UUID shopId);
    List<ShopDto> getAllShops();
    List<ShopDto> getShopsByVendor(UUID vendorId);
}
