package com.example.ktech_project_3.shop.entity;

import com.example.ktech_project_3.entity.BaseEntity;
import com.example.ktech_project_3.order.entity.OrderEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "product")
    private final List<OrderEntity> orders = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private ShopEntity shop;
}
