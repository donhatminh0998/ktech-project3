package com.example.ktech_project_3.order.dto;

import com.example.ktech_project_3.order.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long orderId;
    private String item;
    private Integer quantity;
    private String status;
    private LocalDateTime orderTime;
    private Long productId;
    private String shopName;

    public static OrderDto fromEntity(OrderEntity entity) {
        return OrderDto.builder()
                .orderId(entity.getId())
                .item(entity.getProduct().getNameItem())
                .quantity(entity.getQuantity())
                .status(entity.getStatus())
                .orderTime(entity.getOrderTime())
                .productId(entity.getProduct().getId())
                .shopName(entity.getShop().getShopName())
                .build();
    }
}
