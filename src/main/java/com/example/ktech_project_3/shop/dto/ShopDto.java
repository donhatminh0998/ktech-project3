package com.example.ktech_project_3.shop.dto;

import com.example.ktech_project_3.ItemCategory;
import com.example.ktech_project_3.shop.entity.ShopEntity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShopDto {
    private Long id;
    private String shopName;
    private String description;
    private String shopStatus;

    private ItemCategory category;
    private String owner;
    private String email;
    private String phoneNumber;

    public static ShopDto fromEntity(ShopEntity entity) {
        return ShopDto.builder()
                .id(entity.getId())
                .shopName(entity.getShopName())
                .description(entity.getDescription())
                .owner(entity.getUser().getUsername())
                .category(entity.getCategory())
                .shopStatus(entity.getShopStatus())
                .email(entity.getUser().getEmail())
                .phoneNumber(entity.getUser().getPhoneNumber())
                .build();
    }
}