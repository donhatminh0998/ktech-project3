package com.example.ktech_project_3.jwt.dto;

import lombok.Data;

@Data
public class JwtRequestDto {
    private String username;
    private String password;
}
