package com.example.ktech_project_3.shop.dto;

import com.example.ktech_project_3.shop.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductViewDto {
    private String nameItem;
    private String image;
    private Integer price;
    private String description;
    private String shopName;
    private Long shopId;


    public static ProductViewDto fromEntity(Product entity){
        return ProductViewDto.builder()
                .nameItem(entity.getNameItem())
                .image(entity.getImage())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .shopName(entity.getShop().getShopName())
                .shopId(entity.getShop().getId())
                .build();
    }

}
