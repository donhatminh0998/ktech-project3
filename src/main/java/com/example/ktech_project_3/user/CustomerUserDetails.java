package com.example.ktech_project_3.user;

import com.example.ktech_project_3.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String name;
    private Integer age;
    private String email;
    private String phone;
    private String profileImage;
    private String userRole;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(userRole.split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public static CustomerUserDetails fromEntity(UserEntity entity) {
        CustomerUserDetails details = new CustomerUserDetails();
        details.id = entity.getId();
        details.username = entity.getUsername();
        details.password = entity.getPassword();
        details.name = entity.getName();
        details.age = entity.getAge();
        details.email = entity.getEmail();
        details.phone = entity.getPhone();
        details.profileImage = entity.getProfileImage();
        details.userRole = entity.getUserRole();
        return details;
    }
}
