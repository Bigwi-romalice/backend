package com.shopery.vendor.inventory.services.impl;

import com.shopery.vendor.inventory.dto.AppUsersDto;
import com.shopery.vendor.inventory.dto.ShopDto;
import com.shopery.vendor.inventory.models.AppUsers;
import com.shopery.vendor.inventory.models.Shops;
import com.shopery.vendor.inventory.repositories.AppUsersRepository;
import com.shopery.vendor.inventory.repositories.ShopsRepository;
import com.shopery.vendor.inventory.services.ShopsService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShopsServiceImpl implements ShopsService {


    private final ShopsRepository shopsRepository;
    private final ModelMapper modelMapper;
    private final AppUsersRepository appUsersRepository;



    @Autowired
    public ShopsServiceImpl(ShopsRepository shopsRepository, ModelMapper modelMapper,AppUsersRepository appUsersRepository) {
        this.shopsRepository = shopsRepository;
        this.modelMapper = modelMapper;
        this.appUsersRepository = appUsersRepository;
    }
    @Transactional
    @Override
    public ShopDto createShop(ShopDto shopDto) {
        Shops shop = new Shops();
        shop.setShopName(shopDto.getShopName());
        shop.setShopAddress(shopDto.getShopAddress());
        shop.setShopLogo(shopDto.getShopLogo());
        shop.setShopSpecialisation(shopDto.getShopSpecialisation());
        shop.setLatitude(shopDto.getLatitude());
        shop.setLongitude(shopDto.getLongitude());
        AppUsers vendor = appUsersRepository.findById(shopDto.getVendorId())
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found with ID: " + shopDto.getVendorId()));
        shop.setVendor(vendor);
        Shops savedShop =  shopsRepository.save(shop);
        ShopDto savedShopDto = new ShopDto();
        savedShopDto = modelMapper.map(savedShop, ShopDto.class);

        return savedShopDto;
    }





    @Override
    public Shops updateShop(UUID shopId, ShopDto shopDto) {
        Optional<Shops> optionalShop = shopsRepository.findById(shopId);
        if (optionalShop.isPresent()) {
            Shops shop = optionalShop.get();
            modelMapper.map(shopDto, shop);
            return shopsRepository.save(shop);
        }
        throw new RuntimeException("Shop not found with ID: " + shopId);
    }

    @Override
    public void deleteShop(UUID shopId) {
        shopsRepository.deleteById(shopId);
    }

    @Override
    public ShopDto getShopById(UUID shopId) {
        Shops shops = shopsRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found with ID: " + shopId));;
        return modelMapper.map(shops, ShopDto.class);

    }

    @Override
    public List<ShopDto> getAllShops() {
        List<Shops> shops = shopsRepository.findAll();
        return shops.stream().map((shop) -> modelMapper.map(shop, ShopDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShopDto> getShopsByVendor(UUID vendorId) {
        List<Shops> shops = shopsRepository.findByVendorUserId(vendorId);
        return shops.stream()
                .map(shop -> {
                    ShopDto dto = modelMapper.map(shop, ShopDto.class);
                    dto.setVendorId(shop.getVendor().getUserId()); // Map vendorId
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
