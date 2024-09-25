package com.example.ktech_project_3.user.dto;

import com.example.ktech_project_3.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private Integer age;
    private String email;
    private String phone;
    private String profileImage;
    private String userRole;

    public static UserDto fromEntity(UserEntity entity) {
        return UserDto.builder()
                .username(entity.getUsername())
                .password("*")
                .name(entity.getName())
                .age(entity.getAge())
                .email(entity.getEmail())
                .phone(entity.getPhoneNumber())
                .profileImage(entity.getProfileImage())
                .userRole(entity.getRole())
                .build();
    }

}
