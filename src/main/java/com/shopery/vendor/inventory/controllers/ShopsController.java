package com.shopery.vendor.inventory.controllers;

import com.shopery.vendor.inventory.dto.ShopDto;
import com.shopery.vendor.inventory.models.Shops;
import com.shopery.vendor.inventory.services.ShopsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shops")
public class ShopsController {

    private final ShopsService shopsService;

    @Autowired
    public ShopsController(ShopsService shopsService) {
        this.shopsService = shopsService;
    }
    @Operation(summary = "Create Shop")
    @PostMapping
    public ResponseEntity<ShopDto> createShop(@RequestBody ShopDto shopDto) {
        return ResponseEntity.ok(shopsService.createShop(shopDto));
    }

    @Operation(summary = "Update Shop")
    @PutMapping("/{shopId}")
    public ResponseEntity<Shops> updateShop(@PathVariable UUID shopId, @RequestBody ShopDto shopDto) {
        return ResponseEntity.ok(shopsService.updateShop(shopId, shopDto));
    }

    @Operation(summary = "Disable Shop")
    @DeleteMapping("/{shopId}")
    public ResponseEntity<Void> deleteShop(@PathVariable UUID shopId) {
        shopsService.deleteShop(shopId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get Shop by ID")
    @GetMapping("/{shopId}")
    public ResponseEntity<ShopDto> getShopById(@PathVariable UUID shopId) {
        return ResponseEntity.ok(shopsService.getShopById(shopId));
    }

    @Operation(summary = "Get All Unique Shops")
    @GetMapping
    public ResponseEntity<List<ShopDto>> getAllShops() {
        List<ShopDto> shops = shopsService.getAllShops();
        return new ResponseEntity<>(shops, HttpStatus.OK);
    }

    @Operation(summary = "Get Shops by Owner")
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ShopDto>> getShopsByOwner(@PathVariable UUID ownerId) {
        List<ShopDto> shops = shopsService.getShopsByVendor(ownerId);
        return ResponseEntity.ok(shops);
    }

}
