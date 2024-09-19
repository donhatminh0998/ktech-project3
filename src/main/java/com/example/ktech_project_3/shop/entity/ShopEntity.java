package com.example.ktech_project_3.shop.entity;

import com.example.ktech_project_3.ItemCategory;
import com.example.ktech_project_3.entity.BaseEntity;
import com.example.ktech_project_3.order.entity.OrderEntity;
import com.example.ktech_project_3.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopEntity extends BaseEntity {
    private String shopName;
    private String description;
    private String shopStatus;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private final List<Product> products = new ArrayList<>();
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private final List<OrderEntity> orders = new ArrayList<>();
}
