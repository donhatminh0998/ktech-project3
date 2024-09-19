package com.example.ktech_project_3.request.dto;

import com.example.ktech_project_3.ItemCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class OpenRequestDto {
    private String shopName;
    private String description;
    @Enumerated(EnumType.STRING)
    private ItemCategory category;
}