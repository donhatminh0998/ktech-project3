package com.example.ktech_project_3.user.dto;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class UserProfileDto {
    private String name;
    private String age;
    private String email;
    private String phoneNumber;
}
