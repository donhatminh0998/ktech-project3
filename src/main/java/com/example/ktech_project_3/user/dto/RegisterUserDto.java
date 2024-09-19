package com.example.ktech_project_3.user.dto;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String username;
    private String password;
    private String passwordCheck;
}
