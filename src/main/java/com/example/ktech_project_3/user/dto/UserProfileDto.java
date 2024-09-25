package com.example.ktech_project_3.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private String name;
    private String age;
    private String email;
    private String phoneNumber;
}
