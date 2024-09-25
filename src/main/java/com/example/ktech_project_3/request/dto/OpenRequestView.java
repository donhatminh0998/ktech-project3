package com.example.ktech_project_3.request.dto;

import com.example.ktech_project_3.ItemCategory;
import com.example.ktech_project_3.request.entity.OpenRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class OpenRequestView {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;

    private String shopName;
    private String description;
    private ItemCategory category;
    private String requestStatus;
    private String rejectionReason;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime processedAt;

    public static OpenRequestView fromEntity(OpenRequest entity) {
        return OpenRequestView.builder()
                .id(entity.getId())
                .username(entity.getUser().getUsername())
                .email(entity.getUser().getEmail())
                .phoneNumber(entity.getUser().getPhoneNumber())
                .shopName(entity.getShopName())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .requestStatus(entity.getRequestStatus())
                .rejectionReason(entity.getRejectionReason())
                .createdAt(entity.getCreatedAt())
                .processedAt(entity.getProcessedAt())
                .build();
    }
}
