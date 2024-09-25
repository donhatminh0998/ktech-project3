package com.example.ktech_project_3.user;

import com.example.ktech_project_3.shop.entity.ShopEntity;
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

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUserDetails implements UserDetails {
    @Getter
    private Long id;
    private String username;
    private String password;
    private String role;
    private ShopEntity shop;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(role.split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public static CustomerUserDetails fromEntity(UserEntity entity) {
        CustomerUserDetails details = new CustomerUserDetails();
        details.id = entity.getId();
        details.username = entity.getUsername();
        details.password = entity.getPassword();
        details.role = entity.getRole();
        details.shop = entity.getShop();
        return details;
    }
}
