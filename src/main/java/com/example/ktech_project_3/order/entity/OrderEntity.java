package com.example.ktech_project_3.order.entity;

import com.example.ktech_project_3.entity.BaseEntity;
import com.example.ktech_project_3.shop.entity.Product;
import com.example.ktech_project_3.shop.entity.ShopEntity;
import com.example.ktech_project_3.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderEntity extends BaseEntity {
    private Integer quantity;
    private String status;
    private LocalDateTime orderTime;
    private Integer totalMoney;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private ShopEntity shop;
}
