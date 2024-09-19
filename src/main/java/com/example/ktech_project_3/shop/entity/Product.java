package com.example.ktech_project_3.shop.entity;

import com.example.ktech_project_3.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {
    private String nameItem;
    private String image;
    private Integer price;
    private Integer stock;
    private String description;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private ShopEntity shop;
}
