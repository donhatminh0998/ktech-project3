package com.example.ktech_project_3.request.entity;

import com.example.ktech_project_3.entity.BaseEntity;
import com.example.ktech_project_3.shop.entity.ShopEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class CloseRequest extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "shop_id")
    private ShopEntity shop;

    private String status;

    private String reason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime processedAt;
}
