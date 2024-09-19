package com.example.ktech_project_3.user.entity;

import com.example.ktech_project_3.order.entity.OrderEntity;
import com.example.ktech_project_3.shop.entity.ShopEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_table")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String username;
    @Setter
    private String password;

    @Setter
    private String name;
    @Setter
    private Integer age;
    @Setter
    private String email;
    @Setter
    private String phone;
    @Setter
    private String profileImage;

    @Setter
    private String userRole;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ShopEntity shop;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<OrderEntity> orderList = new ArrayList<>();

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private final List<OpenRequest> openRequests = new ArrayList<>();
}
