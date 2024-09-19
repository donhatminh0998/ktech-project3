package com.example.ktech_project_3.request.entity;

import com.example.ktech_project_3.ItemCategory;
import com.example.ktech_project_3.entity.BaseEntity;
import com.example.ktech_project_3.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class OpenRequest extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String shopName;
    private String description;

    @Enumerated(EnumType.STRING)
    private ItemCategory category;


    private String requestStatus;
    private String rejectionReason;


    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
}
