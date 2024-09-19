package com.example.ktech_project_3.shop.dto;

import com.example.ktech_project_3.shop.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String item;
    private String image;
    private Integer price;
    private Integer stock;
    private String description;

    public static ProductDto fromEntity(Product entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .item(entity.getNameItem())
                .image(entity.getImage())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .description(entity.getDescription())
                .build();
    }
}
